Built a bot to register for UW classes before anyone else. 

If you're looking to use this, the workflow is like this. Specify your courses and the quarter like this: 
```
String[] courseArray = {"BIOL 180", "CHEM 237", "PHYS 122"};
Plan plan = new Plan(courseArray, driver, "autumn", false, false); 
plan.createPlans(1000); 
```

This will then give you your SLN codes. 

Next, comment out the plan code, and uncomment the code below it. Replace the SLN codes with the ones the program gave you. 

Run this on the-day of registration and uncomment the line inside Main.action. You'll have to login yourself, because of two-factor authentication, but after that, this will help keep the page alive in the night. Schedule this time to be ~2-3 minutes before your registration is. 
```
help.keepPageActive(driver, "05:57");
```
