#File Manager

##Overview
File Manager supports all basic file operations.
Main window contains two panes in order to copy and move selected items between them.
Advanced operations are available via text fields above panes.

##Main window
![main_window](http://i.imgur.com/zFbYK3s.png)
Main window contains two panes with corresponding text fields.
Every text field shows current path and can be used to enter advanced quick commands. 

##File operations
* Create new directory

* Create new file

* Rename selected item

* Delete selected items

* Copy selected items to directory on other pane

* Move selected items to directory on other pane

![file_operations](http://i.imgur.com/32OzK40.png)

##Advanced operations
Advanced commands are entered to text fields and are applied to corresponding pane.
**_< command> ::= < keyword> < argument>_**  
**_< argument> ::= < regex> | < word to find>_**  
For example, command **_copy .\*mus.\*_** copies all files that contains _mus_.

![delete_demo](http://i.imgur.com/8IOO58d.png)

#####SELECT

Select all elements that matches regex or contains entered word . 
After selection it is possible to perform other actions.
#####MOVE
Apply _SELECT_ with _moving_.

#####COPY
Apply _SELECT_ with _copying_.

#####DELETE

Apply _SELECT_ with _deletion_.

#####OPEN

Apply _SELECT_ with opening all selected items with associated programs.

##HTML editor
To open HTML files with build-in editor just press *F3* with selected file.

![html_editor](http://i.imgur.com/hYbQ05i.png)

##Words count
To count words in selected .txt file, just press button *Count words*. New txt file file counted words will
appear in the same directory.

![words_count](http://i.imgur.com/EYtPArz.png)

##Observing directory changes
Background thread is used to watch active directories changes. Java Path API is used to do so in effective
 way instead of just pulling directory for changes every time interval.

## Hotkeys
F3 - open with HTML editor  
F5 - copy  
F6 - move  
Delete - delete  
CTRL + N - new file  
CTRL + SHIFT + N - new directory  
SHIFT + D - focus corresponding text field

######Copyright © 2016 by Vitaliy Kononenko, K-24
