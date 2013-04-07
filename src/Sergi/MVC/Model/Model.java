package Sergi.MVC.Model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import Sergi.MVC.Controller.MainFrameObserverInterface;
import Sergi.MVC.Model.parse.XMLreadWrite;

/**
 * @author Sergienko Oleg
 * 
 */
public class Model extends Sergi.MVC.Tools {

	private ArrayList<MainFrameObserverInterface> addEditObservers = new ArrayList<MainFrameObserverInterface>();
	private ArrayList<Task> arrList; ///���� ������ �����
	private HashMap<Date, LinkedList<Task>> shadowMap;// ����� ��� �������� 
	                                  // (���� ��������� ������, ������� ���� �������� �� 5 ���.) 
	                                  // � �����, ����� ���� ������ ������ "��������"
	private String fileName = "Tasks.xml";
	private XMLreadWrite analyzer;
	private TaskChecking taskCheking;
	private Thread thread;

	public Model() throws ModelException {
		arrList = new ArrayList<Task>();
		arrList = readTasksFromFile();
		taskCheking = new TaskChecking(this);
	}

	/** 
	 * ���������� ����� ������ � ���� ������
	 * @param task ������, ������� ���� ��������
	 * @throws ModelException ������, ����� ����������� ������ ��� ������������ � ������
	 */
	public void addNewTask(Task task) throws ModelException {
	    if(arrList.contains(task)) 
	        throw new ModelException("������ ������ ��� ���������.");
		arrList.add(task);
		notifyObservers(arrList);
	}

	/**
	 * ���������� ��������� ����� � ���� ������
	 * @param tasks ������ �����, ������� ���� ��������
	 * @throws ModelException ������, ����� ����������� ������ ��� ������������ � ������
	 */
	public void addTaskArray(Task[] tasks) throws ModelException {
		for (int i = 0; i < tasks.length; i++) {
			addNewTask(tasks[i]);
		}
		notifyObservers(arrList);
	}

	/**
	 * �������� ������ �� ���� ������
	 * @param task ������, ������� ������ ���� �������
	 * @throws ModelException ������, ����� ����� ������ ��� � ���� ������
	 */
	public void removeTask(Task task) throws ModelException {
	    if(!arrList.contains(task))
	        throw new ModelException(
	                "������ ��������. ������ \"" + task.getTitle() + 
	                "\" �� ������� � ���� ������");
		arrList.remove(task);
		notifyObservers(arrList);
	}

	/**
     * ������ ������ �����, ������� ������������ � ���� ������ 
     * @return ������ �����, ������� ������������ � ���� ������
     */
	public ArrayList<Task> getTaskList() {
		return arrList;
	}

	/**
	 * ����� ������ � ���� ������
	 * @param name ��� ������, ������� ����� ����� (����� ������� ������ ���������� ������)
	 * @return ������ �� � �����, ���� null - ���� ������ �� ���� ������� 
	 */
	public Task getTask(String name) {
		for (Task task : arrList)
			if (task.getTitle().equals(name))
				return task;
		return null;
	}
	
	/**
	 * ����������� ����������� �� �������
	 * @param observer
	 */
	public void registerObserver(MainFrameObserverInterface observer) {
		addEditObservers.add(observer);
	}

	/**
	 * �������� ����������� �� �������
	 * @param observer
	 */
	public void removeObserver(MainFrameObserverInterface observer) {
		addEditObservers.remove(observer);
	}

	/**
	 * ���������� ������������ �� ����������
	 * @param value
	 */
    public void notifyObservers(Object value) {
        for (MainFrameObserverInterface iterable_element : addEditObservers) {
            iterable_element.update(value);
        }
    }

    /**
     * ���������� ������������ �� ����������
     */
    public void notifyObservers() {
        for (MainFrameObserverInterface iterable_element : addEditObservers) {
            iterable_element.update(getTaskList());
        }
    }

    /**
     * ���������� ������ ����� �� XML ����� Tasks.xml
     * @return
     * @throws ModelException
     */
	private ArrayList<Task> readTasksFromFile() throws ModelException {
		analyzer = new XMLreadWrite();
		try {
            return analyzer.parseXMLFile(fileName);
        } catch (DOMException e) {
            throw new ModelException(e);
        } catch (FileNotFoundException e) {
            throw new ModelException(e,"XML file with saved taskList not found. "
                    + "Please, check file \"Task.xml\" in folder with program.");
        } catch (ParseException e) {
            throw new ModelException(e);
        } catch (SAXException e) {
            throw new ModelException(e);
        } catch (IOException e) {
            throw new ModelException(e);
        } catch (ParserConfigurationException e) {
            throw new ModelException(e);
        }
	}

	/**
	 * ������ ������ ����� � XML ���� Tasks.xml (������ ������ ������������)
	 * @param taskArray
	 * @return
	 * @throws ModelException
	 */
	public Document writeTasksToFile() throws ModelException {
		if (arrList.size() == 0)
			return null;
		try {
			analyzer = new XMLreadWrite();
			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(analyzer.createDocument(arrList));
			StreamResult result = new StreamResult(new File(fileName));

			transformer.transform(source, result);

		} catch (TransformerConfigurationException e) {
			throw new ModelException(e);
		} catch (TransformerException e) {
			throw new ModelException(e);
		} catch (ParserConfigurationException e) {
			throw new ModelException(e);
		}
		return null;
	}

	/**
	 * ���������� ����� ������� ������ � ���� ������ �� � ��������
	 * @param taskTitle
	 * @return
	 * @throws ModelException
	 */
	public int getTaskIndex(String taskTitle) throws ModelException {
		for (int i = 0; i < arrList.size(); i++) {
			if (taskTitle.equals(arrList.get(i).getTitle()))
				return i;
		}
		throw new ModelException("���������� ����� ������");
	}
	
	/**
	 * ���������� ����� ������� ������ � ���� ������
	 * @param task
	 * @return
	 * @throws ModelException
	 */
   public int getTaskIndex(Task task) throws ModelException {
        for (int i = 0; i < arrList.size(); i++) {
            if (task.equals(arrList.get(i)))
                return i;
        }
        throw new ModelException("���������� ����� ������ � ���� ������");
    }

   /**
    * �����, ������� ������� �������������� ���� � ��������, ������� ���������
    * (��� ���������� � �������� ���������)
    * @param onsetTaskList
    */
   public void itsTimeToTask(LinkedList<Task> onsetTaskList) {
	    System.out.println("ItsTime: "+ onsetTaskList);
	    if(!onsetTaskList.isEmpty()) {
	        notifyObservers(onsetTaskList);
	        notifyObservers(arrList);
	    }
	}
	
   /**
    * ������ ������������ ����� ��� ������ ��, ����� ������ �� �����
    */
   public void startTaskCheking() {
		thread = new Thread(taskCheking);
		thread.start();
	}
	
   /**
    * ����� ���������� ������ �� ���� ������ �� � ������
    * @param selectedIndex
    * @return
    */
   public Task getTaskByIndex(int selectedIndex) {
		return arrList.get(selectedIndex);
	}

   /**
    * ��������, ���������� �� ������ � ���� ������
    * @param chekingTask
    * @return
    */
   public boolean contains(Task chekingTask) {
		for (Task task : arrList) {
			if(task.equals(chekingTask))
				return true;
		}
		return false;
	}

   /**
    * ������ ���������� � ������
    * @param oldTaskData ������ ������
    * @param newTaskData ����� ������
    * @throws ModelException
    */
    public void replaceTask(Task oldTaskData, Task newTaskData) throws ModelException {
        if(newTaskData == null) 
            throw new ModelException("������ ��������� ���������� ������. ����� ������ �� �������.");
        if(getTaskList().contains(newTaskData))
            throw new ModelException("������ ��� ������������ � ������");

        oldTaskData.setTitle(newTaskData.getTitle());
        oldTaskData.setActive(newTaskData.isActive());
        oldTaskData.setRepeatCount(newTaskData.getRepeatCount());
        oldTaskData.setStartTime(newTaskData.getStartTime());
        if(newTaskData.isRepeated())
            oldTaskData.setEndTime(newTaskData.getEndTime());
        notifyObservers(arrList);
    }

    //================������ ������ �� ��������============
    
    /**
     * 
     * @param date
     * @param task
     */
    public void shadowTasksAdd(Date date, Task task) {
        LinkedList<Task> list = new LinkedList<Task>();
        if(shadowMap == null) {
            shadowMap = new HashMap<Date, LinkedList<Task>>();
        }
        
        list.add(task);
        if(shadowMap.containsValue(list))
            return;
        if(!shadowMap.isEmpty() &&
                !shadowMap.get(toDateFormat(date)).isEmpty() && 
                !shadowMap.get(toDateFormat(date)).contains(task)) {
            list.addAll(shadowMap.get(toDateFormat(date)));
            shadowMap.put(toDateFormat(date), list);
        }
        System.out.println("[" + toCurentDateFormat() + "]: shadowTaskAdded: " + date + " " + task);
    }
    
    public void shadowTasksRemove() {
        shadowTasksRemove(toDateFormat(Calendar.getInstance().getTime()));
    }
    
    public void shadowTasksRemove(Date date) {
        shadowMap.remove(toDateFormat(date));
    }
    
    public void shadowTasksRemove(Date date, Task task) {
        if(shadowMap.containsKey(toDateFormat(date)))
            shadowMap.get(toDateFormat(date)).remove(task);
    }
    
    public boolean shadowTasksIsEmpty() {
        return shadowMap.isEmpty() || shadowMap == null;
    }
    
    /**
     * ��������, ���������� �� ������� ������ � ������ ��� ���
     * ���� ����� ������ ����, �� � ��������.
     * @param date ����, � ������� ��������� ������ (����, ����� ������� ������ ������ ���� ��������)
     * @return ������� ������, ������� ������ ��������; ��� null ���� ����� ������� ����� ���
     */
    public LinkedList<Task> shadowTaskCheck(Date date) {
        if(shadowMap != null && shadowMap.containsKey(date)) {
            return shadowMap.get(toDateFormat(date));
        }        
        return null;
    }
    
    public void shadowTaskCheckList(Date date) {
        if(shadowMap != null &&
                !shadowMap.isEmpty() &&
                shadowMap.containsKey(toDateFormat(date)))
            itsTimeToTask(shadowMap.get(toDateFormat(date)));
    }
    
    private boolean shadowTasksContainsTask(LinkedList<Task> onsetTaskList) {
        if(shadowMap == null || shadowMap.isEmpty())
            return false;
        Set<Entry<Date, LinkedList<Task>>> allShadowScope = shadowMap.entrySet();
        for (Entry<Date, LinkedList<Task>> entry : allShadowScope) {
            for (Task task : entry.getValue()) {
                for (Task onsetTask : onsetTaskList) {
                    if(onsetTask.equals(task))
                        return true;
                }
            }
        }
        return false;
    }
}
