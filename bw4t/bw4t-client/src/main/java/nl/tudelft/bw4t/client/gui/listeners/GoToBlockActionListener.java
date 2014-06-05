package nl.tudelft.bw4t.client.gui.listeners;

import java.awt.event.ActionEvent;
import java.util.LinkedList;

import nl.tudelft.bw4t.client.controller.ClientController;
import nl.tudelft.bw4t.client.startup.Launcher;
import eis.iilang.Numeral;
import eis.iilang.Percept;

/**
 * ActionListener that performs the goTo action when that command is pressed in
 * the pop up menu
 */
public class GoToBlockActionListener extends ClientActionListener {
    private final long boxID;

    public GoToBlockActionListener(Long boxID, ClientController control) {
        super(control);
        this.boxID = boxID;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!Launcher.getEnvironment().isConnectedToGoal()) {
            try {
                getController().getHumanAgent().goToBlock(boxID);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        } else {
            LinkedList<Percept> percepts = new LinkedList<Percept>();
            Percept percept = new Percept("goToBlock", new Numeral(boxID));
            percepts.add(percept);
            getController().setToBePerformedAction(percepts);
        }

    }
}
