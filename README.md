# ListExecutables
This jar will find all the files in a folder, and all subfolders, that are executable by Windows.

To run the App :

- Download this executable jar file.
- Store in the top-level Windows folder to be searched.
- Double click the jar file from Windows Explorer.
- Or, to run the app with Admin privilage start a Command Prompt window as the Admin user, and run the app using the lines

        java -jar ListExecutables.jar
   
If you have files that are executable only as Administrator, then run the app with a user that has Admin privilages. The app cannot find files that it does not have permission to access, and it cannot search folders that need Admin privilage unless its run with Admin privilage.

The output will be a file called *executable.csv*. This is an ordered list of all files which are executable by this user.

If the top-level folder includes a file called *master.csv*, then the list of discovered executables is appended to this *master.csv* file.
The *master.csv* file is also ordered.

