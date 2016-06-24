# SeleniumInterpreter

Java interpreter that spawns and controls a firefox broswer instance.  So far, no classes that require imports can be used except for classes in org.openqa.selenium package.

Reference the firefox window using the object reference 'driver'

The window consists of an input text area on top, a stdout area below, and a run button.

input text area - Type java code into here, this code will be injected into a non-returning static method template that provides one parameter called 'driver' of type WebDriver.
output text area - When the input code has finished running, all it's stdout will be printed in this text area.
run button - Compile the code and template and invoke the method containing the input text area's code snippet.
