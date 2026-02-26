boolean falling = false;
long airTimeStart = 0;
long landTimeStart = 0;
long sprintTimer = 0;
boolean needsSprint = false;

void onLoad() {
    modules.registerSlider("Enable Delay", 20, 0, 100, 1);
    modules.registerSlider("Disable Delay", 50, 0, 100, 1);
    modules.registerSlider("Sprint Re-enable", 150, 0, 500, 1);
    modules.registerSlider("Min Fall Distance", 4, 1, 10, 1);
}

void onRenderTick(float partialTicks) {
    Entity p = client.getPlayer();
    if (p == null) return;

    if (client.isSpectator()) {
        if (myau.isEnabled("Scaffold")) {
            myau.setEnabled("Scaffold", false);
        }
        return;
    }

    int dist = getFallDist();
    double enDelay = modules.getSlider("Clutch", "Enable Delay");
    double disDelay = modules.getSlider("Clutch", "Disable Delay");
    double sprDelay = modules.getSlider("Clutch", "Sprint Re-enable");
    double minFall = modules.getSlider("Clutch", "Min Fall Distance");
    
    if (!p.onGround() && p.getMotion().y < -0.01) {
        if (airTimeStart == 0) airTimeStart = client.time();
        
        if (dist == -1 || dist >= (int)minFall) {
            if (!myau.isEnabled("Scaffold")) {
                if (client.time() - airTimeStart >= (long)enDelay) {
                    myau.setEnabled("Scaffold", true);
                    myau.setEnabled("Sprint", false);
                    falling = true;
                    needsSprint = true;
                    sprintTimer = 0;
                }
            }
        }
    } else if (p.onGround()) {
        airTimeStart = 0;
    }

    if (falling && (p.onGround() || (dist != -1 && dist < 1))) {
        if (landTimeStart == 0) landTimeStart = client.time();

        if (myau.isEnabled("Scaffold")) {
            if (client.time() - landTimeStart >= (long)disDelay) {
                myau.setEnabled("Scaffold", false);
                falling = false;
                landTimeStart = 0;
                sprintTimer = client.time();
            }
        }
    }

    if (!falling && needsSprint && sprintTimer != 0) {
        if (client.time() - sprintTimer >= (long)sprDelay) {
            myau.setEnabled("Sprint", true);
            needsSprint = false;
            sprintTimer = 0;
        }
    }
}

int getFallDist() {
    Vec3 pos = client.getPlayer().getPosition();
    int startY = (int) Math.floor(pos.y) - 1;
    if (startY < 0) return -1;

    for (int i = startY; i > Math.max(-1, startY - 10); i--) {
        Block b = world.getBlockAt(new Vec3(Math.floor(pos.x), i, Math.floor(pos.z)));
        if (!b.name.equals("air") && !b.name.contains("sign") && !b.name.contains("torch")) {
            return startY - i;
        }
    }
    return -1;
}