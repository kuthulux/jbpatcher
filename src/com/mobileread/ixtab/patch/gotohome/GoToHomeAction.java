package com.mobileread.ixtab.patch.gotohome;

public class GoToHomeAction extends com.amazon.ebook.booklet.reader.sdk.ui.g {
    public GoToHomeAction(){
        putValue("Name", "Home");
    }
    
    public int dB() {
        return 0;
    }

    public int Vc() {
        return 1;
    }

    public void actionPerformed(java.awt.event.ActionEvent a) {
        new com.amazon.kindle.swing.actions.ActionManager().get(com.amazon.kindle.swing.actions.ActionManager.SWITCH_TO_HOME_ACTION).actionPerformed(a);
    }
}

