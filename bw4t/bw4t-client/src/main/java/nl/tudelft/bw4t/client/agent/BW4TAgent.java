package nl.tudelft.bw4t.client.agent;

import java.rmi.RemoteException;
import java.util.LinkedList;

import nl.tudelft.bw4t.client.environment.PerceptsHandler;
import nl.tudelft.bw4t.client.environment.RemoteEnvironment;
import nl.tudelft.bw4t.client.message.BW4TMessage;
import nl.tudelft.bw4t.client.message.MessageTranslator;
import eis.eis2java.exception.TranslationException;
import eis.eis2java.translation.Translator;
import eis.exceptions.ActException;
import eis.exceptions.PerceiveException;
import eis.iilang.Action;
import eis.iilang.Parameter;
import eis.iilang.Percept;

/**
 * Java agent that can control an entity
 */
public class BW4TAgent extends Thread implements ActionInterface {

	/**
	 * Information storage about the agent.
	 */
    protected String agentId, entityId;
    protected boolean environmentKilled;
    private RemoteEnvironment bw4tenv;

    /**
     * Create a new BW4TAgent that can be registered to an entity.
     * 
     * @param agentId
     *            , the id of this agent used for registering to an entity.
     * @param env
     *            the remote environment.
     */
    public BW4TAgent(String agentId, RemoteEnvironment env) {
        this.agentId = agentId;
        this.bw4tenv = env;
    }
    
    /**
     * Register an entity to this agent
     * 
     * @param entityId
     *            , the Id of the entity
     */
    public void registerEntity(String entityId) {
        this.entityId = entityId;
    }

    /**
     * Run the reasoning process of this agent
     */
    public void run() {
        if (environmentKilled) {
            return;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void goTo(double x, double y) throws ActException {
        try {
            Parameter[] xParam = Translator.getInstance().translate2Parameter(x);
            Parameter[] yParam = Translator.getInstance().translate2Parameter(y);
            Parameter[] parameters = new Parameter[2];
            parameters[0] = xParam[0];
            parameters[1] = yParam[0];
            bw4tenv.performEntityAction(entityId, new Action("goTo", parameters));
        } catch (RemoteException | TranslationException e) {
            ActException ex = new ActException("goTo", e);
            ex.setType(ActException.FAILURE);
            throw ex;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void goToBlock(long id) throws ActException {
        try {
            Parameter[] idParam = Translator.getInstance().translate2Parameter(id);
            bw4tenv.performEntityAction(entityId, new Action("goToBlock", idParam));
        } catch (TranslationException | RemoteException e) {
            ActException ex = new ActException("goToBlock failed", e);
            ex.setType(ActException.FAILURE);
            throw ex;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void goTo(String navPoint) throws ActException {
        try {
            Parameter[] idParam = Translator.getInstance().translate2Parameter(navPoint);
            bw4tenv.performEntityAction(entityId, new Action("goTo", idParam));
        } catch (TranslationException | RemoteException e) {
            ActException ex = new ActException("goTo failed", e);
            ex.setType(ActException.FAILURE);
            throw ex;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void pickUp() throws ActException {
        try {
            bw4tenv.performEntityAction(entityId, new Action("pickUp"));
        } catch (RemoteException e) {
            ActException ex = new ActException("pickUp failed", e);
            ex.setType(ActException.FAILURE);
            throw ex;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void putDown() throws ActException {
        try {
            bw4tenv.performEntityAction(entityId, new Action("putDown"));
        } catch (RemoteException e) {
            ActException ex = new ActException("putDown failed", e);
            ex.setType(ActException.FAILURE);
            throw ex;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendMessage(String receiver, BW4TMessage message) throws ActException {
        this.sendMessage(receiver, MessageTranslator.translateMessage(message));
    }

    /**
     * Sends a message to certain other agents
     * 
     * @param message
     *            , the translated message (as String)
     * @param receiver
     *            , a receiver (can be either all or the id of another agent)
     * @throws ActException
     * 			  , if an attempt to perform an action has failed.
     */
    private void sendMessage(String receiver, String message) throws ActException {
        try {
            Parameter[] messageParam = Translator.getInstance().translate2Parameter(message);
            Parameter[] receiverParam = Translator.getInstance().translate2Parameter(receiver);
            Parameter[] parameters = new Parameter[2];
            parameters[0] = receiverParam[0];
            parameters[1] = messageParam[0];
            bw4tenv.performEntityAction(entityId, new Action("sendMessage", parameters));
        } catch (RemoteException | TranslationException e) {
            ActException ex = new ActException("putDown failed", e);
            ex.setType(ActException.FAILURE);
            throw ex;
        }
    }

    /**
	 * Get all percepts for the associated entity
	 * 
	 * @return a list of percepts
	 * @throws PerceiveException if there was a problem retrieving the percepts.
	 */
	public LinkedList<Percept> getPercepts() throws PerceiveException {
	    return (LinkedList<Percept>) PerceptsHandler.getAllPerceptsFromEntity(entityId, bw4tenv);
	}

	public void setKilled() {
        environmentKilled = true;
    }
	
	public String getAgentId() {
	    return agentId;
	}

	public RemoteEnvironment getEnvironment() {
	    return bw4tenv;
	}

}