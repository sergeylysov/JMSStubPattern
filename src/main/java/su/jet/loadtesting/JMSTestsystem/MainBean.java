/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package su.jet.loadtesting.JMSTestsystem;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;

/**
 *
 * @author sergey
 */
@Named(value = "mainBean")
@SessionScoped
public class MainBean implements Serializable {
    private int delay;
    private int threadCount;
    /**
     * Creates a new instance of MainBean
     */
    
    public void setDelay(int delay){
        this.delay = delay;
        TestSystem.setDelay(delay);
    }

    public int getDelay() {
        
        return TestSystem.getDelay();
    }
    
    public void confirmDelay(){
        System.out.println("confirm delay "+ delay);
    }
    public MainBean() {
    }

    public int getThreadCount() {
        return TestSystem.getThreadCount();
        
    }
    public void incrementThreads(){
        TestSystem.incrementThreads();
    }
    public void decrementThreads(){
        TestSystem.decrementThreads();
    }
    public int getCounter(){
       return TestSystem.getCount();
    }
    public void resetCounter(){
        TestSystem.resetCount();
    }
    public void updateCounter(){
        
    }
            
}
