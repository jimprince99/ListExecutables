# ListExecutables
Find all the files in a folder, and subfolders, that are executable by Windows.

Download this executable jar file.
Store in the top-level Windows folder to be searched.
Run the app using the lines
   java -jar ListExecutables.jar
If you have files that are executable only as Administrator, then run the app with a used that has Admin privilages.

The output will be a file called executable.csv. This is an ordered list of all files which as executable by this user.

If the folder includes a file called master.csv, then the list of discovered executables is appended to this master.csv file.
The master.csv file is also ordered.

