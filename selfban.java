boolean onPacketSent(CPacket packet) {
    if (packet instanceof C01) {
        C01 c01 = (C01) packet;
        String[] parts = c01.message.split(" ");

        if (c01.message.startsWith(".selfban")) {
            client.print("Banning...");
            for (int i = 0; i < 9999; i++) {
                client.sendPacketNoEvent(new C09(0));
                client.sendPacketNoEvent(new C07(new Vec3(-1, -2, -1), "RELEASE_USE_ITEM", "UP"));
            }
            return false;
        }
    }
    return true;
}
