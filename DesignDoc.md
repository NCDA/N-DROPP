##NATIONAL COLLEGIATE DODGEBALL ASSOCIATION  
[http://NCDAdodgeball.com](http://NCDAdodgeball.com) - ncdadodgeball@gmail.com - @NCDA

**N-DROPP**  
*Detailed Design Document (D3)*

**Contributors:**  
Zigmas Maloni @zigmister – Project Manager, Designer, Board Liaison  
Vanswa Garbutt @vonstuben – Lead Developer  
[Kyle Peltier](peltikyl12@gmail.com) – Programmer, Designer  
Mario Galeno – UX Design  
Matthew Lukasiewicz – UX Design  
Matthew Williams – UX Design  

##Contents
1. N-DROPP Source Design  
  - Model-View-Controller (MVC)  
- Documentation  
  - Java Files  
    - Header  
    - Classes  
    - Methods  
    - Variables  
  - Notes/Comments  
  - XML Files  
    - View Id’s 
- ShotClock FSM  
- GameClock FSM  
- RANDOM JIBBERISH (NOTES,REMINDERS,ETC.)  


##N-DROPP Source Design
``` Depreciated? 
###Model-View-Controller (MVC)
The N-DROPP coding structure will be in MVC form.  I’d like each segment of the design to have its own thread, therefore making this a multithreaded program.  
  -  The Model thread will be the main thread of the application.  This thread will make sure all of the brainiack work is going smoothly.  
  - The View thread will continually check the values of the Model, updating its displays based on those values.  This will ensure that the graphics of the application are constant and smooth.  
  - It’s difficult to get the Controller to be its own thread but input from the user will directly modify the Model and updates to graphics will follow.  
*! KP: Removed MVC design structure.  A thread that did not create a View object cannot modify it.  Hence, can’t have a “View” thread.
```
``-Is this really true? -zm``

The application will be split into sections by Activities (android.app.Activity).  There will be an activity for each type of referee: Shot Clock Referee ``SCR``, Head Referee ``HR``, and Hybrid (Head referee controls whole game).  Each activity will support the features/functions only necessary for that particular referee. Other Activities will be created for separate screens such as menus (Main menu, Settings, Game setup, etc.)

##Documentation

###Java Files

####Header
All files should begin with the following header:
```java
/**************************************************************************************************
 * _____   __     _______________________________________ 
 * ___  | / /     ___  __ \__  __ \_  __ \__  __ \__  __ \
 * __   |/ /________  / / /_  /_/ /  / / /_  /_/ /_  /_/ /
 * _  /|  /_/_____/  /_/ /_  _, _// /_/ /_  ____/_  ____/
 * /_/ |_/        /_____/ /_/ |_| \____/ /_/     /_/
 * 
 * National Collegiate Dodgeball Association (NCDA)
 * NCDA - Dodgeball Referee Officiating Application
 * http://www.ncdadodgeball.com
 * Copyright 2014. All Rights Reserved.
 *************************************************************************************************/
```
The ASCII art is made possible by http://patorjk.com/software/taag/.

####Classes
Each declared class should contain a description comment in the format of the one below:
```java
/* ClassName
 * Class description should go here. This should be a brief overview
 * of the entire class.  All class names should begin with a capital letter.
 * Notice the details of this comment.  The comment is started by “/*”
 * (single asterisk) followed by the class’ name.  The next line should
 * start the description text.
 */
public class ClassName
{
	...
}
```
	
####Methods
Each declared method should contain a description of the method, its parameters, and the value it’s returning (if any).
```java
/** int methodName ( arg1, arg2 )
 * @param arg1 : very brief description of argument 1
 * @param arg2 : very brief description of argument 2
 * 
 * @return description of the value that will be returned
 * 
 * Method description goes here. All methods should begin with a lowercase
 * letter.  Method comments should begin with “/**” (double asterisk).
 * If the method lacks parameters or a return type, omit them from the
 * comment as necessary.
 */
public int methodName(int arg1, String arg2){
	...
}
```

####Variables
Variable names should be created with Hungarian notation (description of variable in the name).
```java
String    strString;
int		    nNumber;
```

###Notes/Comments
Other source code comments can be denoted either by block comments (/* */) or line comments (//).  If you’re using block comments, make sure the comment begins with a single asterisk (/*) and not two (/**) as denoted by Class comments.


###XML Files
####View Id’s  
All view IDs should begin with an ALL-CAPS acronym of the view’s name.  Following, the view’s name should be preceded with a description (Hungarian notation) similar to Java Variables. ``EX.	MAIN_imgLogo``

##ShotClock FSM

```
States  Name          Description
S1      Paused Top    Clock is not running and its current time starts at the top (full time)
S2      Rolling Top   Clock starts running from the top (full time)
S3      Paused	      Clock is paused
S4	    Resumed	      Clock resumes counting from a pause
S5	    Expired	      Clock timer has reached zero
```
```
Input Event   Name	                       Description
I1            B1 - Reset/Start/Restart    (Button 1): Single input button that represents Start/Reset. (Restart when expired)
I2	          B2 - Pause/Resume/Reset     (Button 2): Single input button that represents Pause/Resume (Reset when expired)
I3	          Expired	                    Triggered event when clock reaches zero
```
```
Output Event	Name	          Description
O1	          B1 > ”reset”    Change B1’s text to “Reset”
O2	          Enable B2       Allow B2 to be clickable
O3	          B2 > ”resume”   Change B2’s text to “resume”
O4	          B2 > “pause”    Change B2’s text to “pause”
O5	          Disable B2      Disable B2 from being clickable
O6	          B1 > “start”    Change B1’s text to “start”
O7            B2 > “reset”    Change B2’s text to “reset”
O8            B1 > “restart”  Change B1’s text to “restart”
```

![FSM Shot Clock](http://ncdadodgeball.com/media/code/officiating-app/images/shot-clock-fsm.png)

##GameClock FSM

```
States	Name        Description
S1	    Paused Top  Clock is not running and its current time starts at the top (full time)
S2	    Running     Clock is running
S3	    Paused      Clock is paused
S5	    Expired     Clock timer has reached zero
```
```
Input Event Name	                    Description
I1	        B1 – Start/Pause/Resume   (Button 1): Single input button that represents Start/Pause/Resume
I2	        rolloverTime/Halftime     Event indicating the current time should be rolled to next half
I3	        Expired	                  Triggered event when clock reaches zero
```
```
Output Event  Name            Description
O1            B1 > ”pause”    Change B1’s text to “Pause”
O6	          B1 > “resume”	  Change B1’s text to “Resume”
O7	          B1 > “start”	  Change B2’s text to “Start”
O8	          Disable B1      B1 is grayed-out and cannot be selected
```
![FSM Game Clock](http://ncdadodgeball.com/media/code/officiating-app/images/game-clock-fsm.png)

##RANDOM JIBBERISH (NOTES,REMINDERS,ETC.)

- In-game statistics
- Audible shot clock. vibration.
- built-in whistle/buzzer
- rulebook - search functions, filters, and categorization for easier rule look-up.
- An answer to a free-response question suggested recording certain game scenarios where discrepancies unveiled. This way, captains and referees could go back and review these situations and make reformed decisions.  This is similar to the idea of recording event history of a game, however, this implementation would require more detail (and effort on the referee's part).
- Allow Head referee to end the game early (2 minutes left, just end the game and submit the scores).  With this, allow a way for the user to recover a game in case the user accidentally ends the game.  To recover a game, it must be the same date as the game was started (or less than 6 hrs if rolled over midnight or something ridiculous like that).  In order to recover a game, information such as Players remaining for both teams, game clock, and scores will have to be stored.
