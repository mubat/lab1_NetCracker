package Sergi.MVC.Controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EventListener;

public class ActionListenerTM implements ActionListener {
	private Object frame;
	private int id;
	private ActionListener parentListener;

	public ActionListenerTM(Object frame, int id) {
		this.frame = frame;
		this.id = id;
	}

	public ActionListenerTM(Object frame, int id, ActionListener event) {
		this.frame = frame;
		this.id = id;
		this.parentListener = event;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		ActionEvent event = new ActionEvent(frame, id, e.getActionCommand());
		parentListener.actionPerformed(event);
	}

	/**
	 * @return the listener
	 */
	public ActionListener getParentListener() {
		return parentListener;
	}

	/**
	 * @param listener
	 *            the listener to set
	 */
	public ActionListener setParentListener(ActionListener listener) {
		this.parentListener = listener;
		return this;
	}
}