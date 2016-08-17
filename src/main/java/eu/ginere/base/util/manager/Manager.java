package eu.ginere.base.util.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import eu.ginere.base.util.test.TestInterface;
import eu.ginere.base.util.test.TestResult;

/**
 * @author ventura
 *
 * This contains a reference to all the manager to to automatica tasks on all of them
 */
public class Manager implements TestInterface{
	
	public static Manager MANAGER=new Manager();
	private Vector<AbstractManager> list=new Vector<AbstractManager>();

	private Manager(){		
	}


	public void subscrive(AbstractManager manager){
		list.add(manager);
	}
	
	@Override
	public TestResult test(){
		TestResult ret=new TestResult(getClass());
		
		for (AbstractManager manager:list){
			ret.add(manager.test());
		}
		return ret;			
	}

//
//	public void clearCache(){
//		for (AbstractManager manager:list){
//			manager.clearCache();
//		}					
//	}

	public List<String> getList(){
		ArrayList<String> ret=new ArrayList<String>(list.size());
		for (AbstractManager manager:list){
			ret.add(manager.getClass().getName());
		}
		return ret;					
	}
	
}
