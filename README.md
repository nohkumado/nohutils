# nohutils
androified utils, had to split it from regular utils, got too much complicated to keep 
them together....

contrary to my usual utils, android comes with inbuild internationalisation 
(not as good as mine :D but still)

so at the moment, the android utils limit themselves to the rudimentary shell.

I use it to either access internal parts of my progs, or be able to thow in a more efficient way to
use programs, (yeah i am old unix user, shell rules etc), just create your own commands
estending from Command, and feed them at startup to the shell.

Lots of stuff to be done to make this efficient, e.g. parsing could be done in an AsyncTask,
so as to the workings to the buildup commands (BTW scripting is allready implemented).

actually you have access to "shell variables", i only added the filesystem commands since 
they are pretty allround and illustrate how this works.

This is work in progess, don't know how to make a real testsuite under android :D so not all the features 
are tested....

The app just fires up 2 TextViews, and lets play with the shell, the code for the shell is 
in the library project under UtilsApp/nohutils.

have fun, and if ever you make this better, don't forget the post me ;)

