package Sergi.MVC.Controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import Sergi.MVC.Tools;
import Sergi.MVC.Model.Model;
import Sergi.MVC.Model.Task;
import Sergi.MVC.Viewer.ButtonNames;
import Sergi.MVC.Viewer.InformDialog;
import Sergi.MVC.Viewer.MainFrame;
import Sergi.MVC.Viewer.TaskDialog;

public class Controller extends Tools implements ActionListener, MainFrameObserverInterface,
        ListSelectionListener {

    static SimpleDateFormat sdf;
    private static Model model;
    private static MainFrame mainFrame;
    private Task taskForEdit;
    private TaskDialog addEditDialog;
    private InformDialog informFrame;

    /**
     * 
     * @throws ClassNotFoundException
     *             if the LookAndFeel class could not be found
     * @throws InstantiationException
     *             if a new instance of the class couldn't be created
     * @throws IllegalAccessException
     *             if the class or initializer isn't accessible
     * @throws UnsupportedLookAndFeelException
     *             if lnf.isSupportedLookAndFeel() is false
     */
    public Controller() throws ModelException {
        model = new Model();
        try {
            setLookAndFeel();
        } catch (ModelException e) {
            MainFrame.showErrorMessage(mainFrame, e.toString());
        }
        mainFrame = new MainFrame("��������� �����");
        this.update(model.getTaskList());
        model.registerObserver(this);
        mainFrame.addActionListener(this);
        mainFrame.addListSelections(this);
        mainFrame.addWindowListener(new WindowListenerTM(this));
        model.startTaskCheking();
        mainFrame.setVisible(true);

    }

    public static void main(String[] args) {
        try {
            new Controller();
        } catch (ModelException e) {
            MainFrame.showErrorMessage(mainFrame, e.toString());
        }
    }

/*  public void showAddDialog(int i) {
        if (i == -1) {
            addEditDialog.setTask(new Task());
        } else {
            taskForEdit = model.getTaskList().get(i);
            addEditDialog.setTask(taskForEdit);
        }
        addEditDialog.addActionListener(this);
        addEditDialog.setVisible(true);

    }
*/
    public void findTaskIndex(String text) {
        int findedTaskInList = model.getTaskIndex(text);
        if (findedTaskInList != -1)
            mainFrame.enableInList(findedTaskInList);
    }

    public void storeTasksAndExit() throws ModelException {
        model.writeTasksToFile(model.getArrayTaskList());
        System.exit(0);
    }

/*    private void setWindowEvent(ActionEvent e) {
        String buttonName = e.getActionCommand();

        if(ButtonNames.BUTTON_NAME_ADD_TASK.getTypeValue().equals(buttonName)  ||
           ButtonNames.BUTTON_NAME_EDIT_TASK.getTypeValue().equals(buttonName) ||
           ButtonNames.BUTTON_NAME_CANCEL_TASK.getTypeValue().equals(buttonName)) {
            addEditDialog = (TaskDialog) e.getSource(); 
        }
        
        if(ButtonNames.BUTTON_NAME_DEACTIVATE.getTypeValue().equals(buttonName) ||
           ButtonNames.BUTTON_NAME_SET_ASIDE.getTypeValue().equals(buttonName)) {
            informFrame = (InformDialog) e.getSource();
        }
    }
*/
    @Override
    public void update(Object value) {
        System.out.println("Update method: " + value.getClass());
        if (value instanceof LinkedList<?>)
            info((LinkedList<Task>) value);
        else if (value instanceof ArrayList<?>)
            mainFrame.updateList(model.getArrayTaskList());
        else error("Unexpectable type.");
    }

    private void info(LinkedList<Task> values) {
/*        if(informFrame == null) {
            informFrame = new InformDialog(mainFrame, false, values);
            informFrame.addActionListener(this);
            informFrame.setVisible(true);
        }
        for (Task task : values) {
            informFrame.addElement(task);
        }
*/      
        for (Task task : values) {
            InformDialog informFrame = new InformDialog(mainFrame, false, task);
            informFrame.setLocationRelativeTo(mainFrame);
            informFrame.addActionListener(this);
            informFrame.setVisible(true);
        }
        update(model.getTaskList());
    }

    @Override
    public void valueChanged(ListSelectionEvent arg0) {
        mainFrame.setButtonEnabled(
                ButtonNames.BUTTON_NAME_REMOVE.getTypeValue(), true);
    }

    private static void setLookAndFeel() throws ModelException {
        try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            throw new ModelException(e, "���� �� ������ �� ������");
        } catch (InstantiationException e) {
            throw new ModelException(e);
        } catch (IllegalAccessException e) {
            throw new ModelException(e);
        } catch (UnsupportedLookAndFeelException e) {
            throw new ModelException(e);
        }
    }

    @Override
    public void actionPerformed(ActionEvent event) {
//      setWindowEvent(event);
        String actionCommand = event.getActionCommand();
        ButtonNames buttonName = ButtonNames.getType(actionCommand);
        ActionHandler handler = this.new ActionHandler();
        System.out.println(actionCommand + ":\t" + event.getSource());
        try {
            switch (buttonName) {
            case BUTTON_NAME_ADD_DIALOG:  handler.addDialog(); break;
            case BUTTON_NAME_REPALCE:     handler.editDialog((MainFrame) event.getSource()); break;
            case BUTTON_NAME_REMOVE:      handler.remove((MainFrame) event.getSource()); break; 
            case BUTTON_NAME_EXIT:        handler.exit();   break;
            case BUTTON_NAME_FIND:        handler.find((MainFrame) event.getSource()); break;
            case BUTTON_NAME_ADD_TASK:    handler.addTask((TaskDialog) event.getSource()); break;
            case BUTTON_NAME_EDIT_TASK:   handler.editTask((TaskDialog) event.getSource()); break;
            case BUTTON_NAME_CANCEL_TASK: handler.cancel((TaskDialog) event.getSource()); break;
            case BUTTON_NAME_SET_ASIDE:   handler.setAside((InformDialog) event.getSource()); break;
            case BUTTON_NAME_DEACTIVATE:  handler.deactivate((InformDialog) event.getSource()); break;
            default:                      error("����������� �������� �������: " + buttonName);
            }
        } catch(ModelException e) {
            error(e.toString());
        }
        model.checkTasks();
    }
    
    private class ActionHandler {

        public void addDialog(){
            TaskDialog addEditDialog1 = new TaskDialog(mainFrame, false);
            addEditDialog1.setTask(new Task());
            addEditDialog1.addActionListener(Controller.this);
            addEditDialog1.setVisible(true);
        }

        public void cancel(TaskDialog object) {
            object.dispose();
        }

        public void editTask(TaskDialog object) throws ModelException {
            if(model.getTaskList().contains(object.getTask())) {
                error("������ ��� ������������ � ������");
                return;
            } 
            model.replaceTask(taskForEdit, object.getTask());
            object.dispose();
            model.checkTasks();
        }

        public void addTask(TaskDialog object) {
            Task task = object.getTask();
            
            if (model.contains(task)) {
                error("������ ��� ���� � ������");
                return;
            }
            model.addNewTask(object.getTask());
            object.dispose();
            model.checkTasks();
            
        }

        public void find(MainFrame object) {
            findTaskIndex(object.getFindString());
        }

        public void remove(MainFrame object) {
            for (Object task : object.getSelectasValuesList()) {
                model.removeTask((Task) task);
            }
        }

        public void editDialog(MainFrame mainFrame){
            if (mainFrame.getSelectedIndicies().length == 1) {
                TaskDialog addEditDialog = new TaskDialog(
                        mainFrame,"�������� ������ " + model.getTaskIndex(mainFrame.getSelectedIndex()).getTitle(), 
                        false);
                taskForEdit = model.getTaskList().get(mainFrame.getSelectedIndex());
                addEditDialog.setTask(taskForEdit);
                addEditDialog.addActionListener(Controller.this);
                addEditDialog.setVisible(true);
            } else
                error("������� ������ ������ ������� ��� ���������!");
        }
        
        public void exit(){
            try {
                storeTasksAndExit();
            } catch (ModelException e) {
                error(e.getMessage());
            }
        }
        
        public void deactivate(InformDialog object) {
            Task taskToDeactivate = object.getTask();
            int i = model.getTaskIndex(taskToDeactivate);
            
            if(i == -1) {
                error("���������� ����� ������ � ���� ������");
                return;
            }
            
            model.setTaskActiveStatus(i, false);
            object.dispose();
        }

        public void setAside(InformDialog object) {
            Task taskToAside = object.getTask();
            
            int i = model.getTaskIndex(taskToAside);
            if(i == -1) {
                error("���������� ����� ������ � ���� ������");
                return;
            }
            
            Task task = model.getTaskList().get(i);
            Date continueTime = model.getTaskList().get(i).getTime();
            continueTime.setTime(Calendar.getInstance().getTime().getTime() + 5 * 60 * 1000);
            
            if(!task.isRepeated()) {
                model.getTaskList().get(i).setTime(continueTime);
            }
            else {
                if(task.getEndTime().after(continueTime))
                    task.setStartTime(continueTime);
                else
                    task.setEndTime(continueTime);
            }
            
            model.notifyObservers();
            object.dispose();
        }

    }
}