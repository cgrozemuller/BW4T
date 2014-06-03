package nl.tudelft.bw4t.client.gui.menu;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import nl.tudelft.bw4t.client.controller.ClientMapController;
import nl.tudelft.bw4t.client.gui.BW4TClientGUI;
import nl.tudelft.bw4t.client.gui.listeners.GoToBlockActionListener;
import nl.tudelft.bw4t.client.gui.listeners.GoToRoomActionListener;
import nl.tudelft.bw4t.client.gui.listeners.MessageSenderActionListener;
import nl.tudelft.bw4t.client.gui.listeners.PickUpActionListener;
import nl.tudelft.bw4t.client.gui.listeners.PutdownActionListener;
import nl.tudelft.bw4t.map.ColorTranslator;
import nl.tudelft.bw4t.map.Zone;
import nl.tudelft.bw4t.map.view.Block;
import nl.tudelft.bw4t.message.BW4TMessage;
import nl.tudelft.bw4t.message.MessageType;

/** Responsible for building certain pop-up menus related to rooms. */
public final class RoomMenus {
	
	/** Should never be instantiated. */
	private RoomMenus() { }
	
    /**
     * Used for building the pop up menu when clicking on the agent while it is near a box
     * 
     * @param box
     *            - The box that the robot is at.
     * @param room
     *            - The room the robot is in.
     * @param gui
     *            - The {@link BW4TClientGUI} to create the pop-up menu on.
     */
    public static void buildPopUpMenuForBeingAtBlock(Block box, Zone room, BW4TClientGUI gui) {
        String label = room.getName();

        gui.getjPopupMenu().removeAll();

        // Robot commands
        BasicMenuOperations.addSectionTitleToPopupMenu("Command my robot to:", gui.getjPopupMenu());

        JMenuItem menuItem = new JMenuItem("Pick up " + box.getColor() + " block");
        menuItem.addActionListener(new PickUpActionListener(gui.getController()));
        gui.getjPopupMenu().add(menuItem);

        // Message sending
        gui.getjPopupMenu().addSeparator();
        BasicMenuOperations.addSectionTitleToPopupMenu("Tell: ", gui.getjPopupMenu());
        BasicMenuOperations.addMenuItemToPopupMenu(new BW4TMessage(MessageType.ROOMCONTAINS, label, box.getColor()
                .getName(), null), gui);
        BasicMenuOperations.addMenuItemToPopupMenu(new BW4TMessage(MessageType.AMGETTINGCOLOR, label, box.getColor()
                .getName(), null), gui);
        BasicMenuOperations.addMenuItemToPopupMenu(new BW4TMessage(MessageType.ATBOX, null, box.getColor().getName(),
                null), gui);

        gui.getjPopupMenu().addSeparator();
        menuItem = new JMenuItem("Close menu");
        gui.getjPopupMenu().add(menuItem);
    }

    /**
     * Used for building a pop up menu when clicking on a box
     * 
     * @param box
     *            The box that was clicked on.
     * @param room
     *            - The room the robot is in.
     * @param gui
     *            - The {@link BW4TClientGUI} to create the pop-up menu on.
     */
    public static void buildPopUpMenuForBlock(Block box, Zone room, BW4TClientGUI gui) {
        String label = room.getName();

        gui.getjPopupMenu().removeAll();

        // Robot commands
        BasicMenuOperations.addSectionTitleToPopupMenu("Command my robot to:", gui.getjPopupMenu());

        JMenuItem menuItem = new JMenuItem("Go to " + box.getColor() + " block");
        menuItem.addActionListener(new GoToBlockActionListener(box.getObjectId(), gui.getController()));
        gui.getjPopupMenu().add(menuItem);

        // Message sending
        gui.getjPopupMenu().addSeparator();
        BasicMenuOperations.addSectionTitleToPopupMenu("Tell: ", gui.getjPopupMenu());
        BasicMenuOperations.addMenuItemToPopupMenu(new BW4TMessage(MessageType.ROOMCONTAINS, label, box.getColor()
                .getName(), null), gui);
        BasicMenuOperations.addMenuItemToPopupMenu(new BW4TMessage(MessageType.AMGETTINGCOLOR, label, box.getColor()
                .getName(), null), gui);

        gui.getjPopupMenu().addSeparator();
        menuItem = new JMenuItem("Close menu");
        gui.getjPopupMenu().add(menuItem);
    }

    /**
     * Used for building a pop up menu when clicking on a room
     * 
     * @param room
     *            - The room that was clicked on.
     * @param gui
     *            - The {@link BW4TClientGUI} to create the pop-up menu on.
     */
    public static void buildPopUpMenuRoom(Zone room, BW4TClientGUI gui) {
        ClientMapController cmc = gui.getController().getMapController();
        Block holding = cmc.getTheBot().getFirstHolding();
        String label = room.getName();
        gui.getjPopupMenu().removeAll();

        // Robot commands
        BasicMenuOperations.addSectionTitleToPopupMenu("Command my robot to: ", gui.getjPopupMenu());

        JMenuItem menuItem = new JMenuItem("Go to " + label);
        menuItem.addActionListener(new GoToRoomActionListener(label, gui.getController()));
        gui.getjPopupMenu().add(menuItem);

        if (holding != null) {
            menuItem = new JMenuItem("Put down box");
            menuItem.addActionListener(new PutdownActionListener(gui.getController()));
            gui.getjPopupMenu().add(menuItem);
        }

        gui.getjPopupMenu().addSeparator();

        // Message sending
        BasicMenuOperations.addSectionTitleToPopupMenu("Tell: ", gui.getjPopupMenu());

        BasicMenuOperations.addMenuItemToPopupMenu(new BW4TMessage(MessageType.INROOM, label, null, null), gui);

        JMenu submenu = BasicMenuOperations.addSubMenuToPopupMenu(label + " contains ", gui.getjPopupMenu());

        for (String color : ColorTranslator.getAllColors()) {
            menuItem = new JMenuItem(color);
            menuItem.addActionListener(new MessageSenderActionListener(new BW4TMessage(MessageType.ROOMCONTAINS, label,
                    color, null), gui.getController()));
            submenu.add(menuItem);
        }

        submenu = BasicMenuOperations.addSubMenuToPopupMenu(label + " contains ", gui.getjPopupMenu());

        for (int i = 0; i < 6; i++) {
            JMenu submenuColor = new JMenu("" + i);
            submenu.add(submenuColor);

            for (String color : ColorTranslator.getAllColors()) {
                menuItem = new JMenuItem(color);
                menuItem.addActionListener(new MessageSenderActionListener(new BW4TMessage(
                        MessageType.ROOMCONTAINSAMOUNT, label, color, i), gui.getController()));
                submenuColor.add(menuItem);
            }
        }

        BasicMenuOperations.addMenuItemToPopupMenu(new BW4TMessage(MessageType.CHECKED, label, null, null), gui);

        submenu = BasicMenuOperations.addSubMenuToPopupMenu(label + " has been checked by ", gui.getjPopupMenu());

        for (String p : gui.getController().getOtherPlayers()) {
            menuItem = new JMenuItem("" + p);
            menuItem.addActionListener(new MessageSenderActionListener(new BW4TMessage(MessageType.CHECKED, label,
                    null, p), gui.getController()));
            submenu.add(menuItem);
        }

        BasicMenuOperations.addMenuItemToPopupMenu(new BW4TMessage(MessageType.ROOMISEMPTY, label, null, null), gui);

        if (holding != null) {
            BasicMenuOperations.addMenuItemToPopupMenu(new BW4TMessage(MessageType.ABOUTTODROPOFFBLOCK, null, holding
                    .getColor().getName(), null), gui);
        } else {
            BasicMenuOperations.addMenuItemToPopupMenu(new BW4TMessage(MessageType.DROPPEDOFFBLOCK, null, null, null),
                    gui);
        }

        if (holding != null) {
            BasicMenuOperations.addMenuItemToPopupMenu(new BW4TMessage(MessageType.HASCOLOR, null, holding.getColor()
                    .getName(), null), gui);

            submenu = BasicMenuOperations.addSubMenuToPopupMenu("I have a " + holding.getColor().getName()
                    + " block from ", gui.getjPopupMenu());

            for (Zone roomInfo : cmc.getRooms()) {
                menuItem = new JMenuItem(roomInfo.getName());
                menuItem.addActionListener(new MessageSenderActionListener(new BW4TMessage(MessageType.HASCOLOR,
                        roomInfo.getName(), holding.getColor().getName(), null), gui.getController()));
                submenu.add(menuItem);
            }
        }

        BasicMenuOperations.addSectionTitleToPopupMenu("Ask:", gui.getjPopupMenu());
        BasicMenuOperations.addMenuItemToPopupMenu(
                new BW4TMessage(MessageType.ISANYBODYGOINGTOROOM, label, null, null), gui);
        BasicMenuOperations.addMenuItemToPopupMenu(new BW4TMessage(MessageType.WHATISINROOM, label, null, null), gui);
        BasicMenuOperations.addMenuItemToPopupMenu(
                new BW4TMessage(MessageType.HASANYBODYCHECKEDROOM, label, null, null), gui);
        BasicMenuOperations.addMenuItemToPopupMenu(new BW4TMessage(MessageType.WHOISINROOM, label, null, null), gui);

        gui.getjPopupMenu().addSeparator();
        menuItem = new JMenuItem("Close menu");
        gui.getjPopupMenu().add(menuItem);
    }

}
