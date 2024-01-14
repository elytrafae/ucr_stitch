package com.cmdgod.mc.ucr_stitch.mixininterfaces;

public interface IPlayerEntityMixin {
    
    public boolean isFirstTickSneak();
    public int getAssistedKillTicks();

    public void togglePVPRequest();
    public boolean getPVPStatus();
    public int getPVPToggleBan();
    public void enablePVP();
    public void disablePVP();
    public void startDisablingPVP();
    public void setPvpToggleBan(int time);
    public void copyPvPData(IPlayerEntityMixin from);

}
