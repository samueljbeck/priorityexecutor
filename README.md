# priorityexecutor

Priority Executor is code that has been modified from multiple other attempts at controlling thread execution.  The main purpose this was written was for controlling important image downloads from non-important image downloads and for prioritizing important calls.


If using Kotlin:
In your Application class

	var appQue = PriorityExecutor.newFixedThreadPool(Integer.MAX_VALUE) as PriorityExecutor


Then to use this:

	
	(applicationContext as ApplicationClass).appQue.run({
			//code goes here
    }, priority)	
	
or for a default of priority 5 use:

	(applicationContext as ApplicationClass).appQue.run {
		//code goes here
	}
	


Add to your app:

	implementation 'com.samueljbeck.utilities:priorityexecutor:0.0.3'
	



