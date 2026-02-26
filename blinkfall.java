/*
 * blink fall script
 * by @maya.gay
 */


void onLoad() {
    modules.setCategory(category.movement);
}

void onDisable() {
    myau.setEnabled("blink", false);
    renderPos = null;
    weDidntStartTheFire = false;
}

Vec3 renderPos = null;
boolean weDidntStartTheFire = false;

void onPreMotion(final PlayerState state) {
    final Entity player = client.getPlayer();
    final boolean blinkState = myau.isEnabled("blink");

    if (player.getFallDistance() > 2.7 || blinkState) {
        if (!blinkState) {
            myau.toggle("blink");
            weDidntStartTheFire = true;
        }

        if (renderPos == null) {
            renderPos = player.getPosition().offset(-0.3, 1.65, -0.3);
        }
    }

    if (player.onGround()) {
        if (blinkState && weDidntStartTheFire) {
            myau.toggle("blink");
            weDidntStartTheFire = false;
        }

        renderPos = null;
    }
}

void onRenderWorld(float partialTicks) {
    if (renderPos != null) {
        render.text3d("REAL", renderPos, 2, true, true, true, 0xFFA0A0A0);
    }
}