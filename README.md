# Food-Analyzer

Program that gives actual information about different foods.

Usage:

Start the server.
Start as many clients as you like and enter an API-key (API-key can be generated from: https://fdc.nal.usda.gov/api-key-signup.html)
Use one of the following server requests : 
get-food <name> / get-food-report <fdcid> / get-food-by-barcode <barcode> / get-food-by-barcode --img <path of image containing barcode>
  
Note: The Rest API doesn't support searching by barcode, in order to use this function of the program the food has to be already searched before by <name> or <fdcid> 
  so it will be stored in the cache of the server.
  
Server request examples

by name :
  get-food raffaello treat
  
by FoodData Central ID (fdcid):
  get-food-report 1223633 
  
by barcode (UPC code) :
  get-food-by-barcode 009542019877
  
by image of barcode  (UPC code) : (Images can be generated here: https://barcode.tec-it.com/en/UPCA)
  get-food-by-barcode --img C:\testFile.gif
