import os, errno


EntityPackage = raw_input("What is the entity package name[default : Entities]>")
if not EntityPackage :
	EntityPackage = "Entities"
	
EndpointPackage = raw_input("What is the endpoint package name>[default : Endpoints]")
if not EndpointPackage :
	EndpointPackage = "Endpoints"
	
UtilsPackage = raw_input("What is the utils package name[default : Utils]>")
if not UtilsPackage :
	UtilsPackage = "Utils"
	
try:
    os.makedirs("Generated")
except OSError as e:
    if e.errno != errno.EEXIST:
        raise	

try:
    os.makedirs("Generated/"+EntityPackage)
except OSError as e:
    if e.errno != errno.EEXIST:
        raise
        
try:
    os.makedirs("Generated/"+EndpointPackage)
except OSError as e:
    if e.errno != errno.EEXIST:
        raise
try:
    os.makedirs("Generated/"+UtilsPackage)
except OSError as e:
    if e.errno != errno.EEXIST:
        raise
   
DBHost = raw_input("What is the URL of the database host[default : localhost]>")
if not DBHost :
	DBHost = "localhost"
	
DBDatabase = raw_input("What is the name of the database[default : test]>")
if not DBDatabase :
	DBDatabase = "test"
	
DBUserName = raw_input("What is the database username[default : root]>")
if not DBUserName :
	 DBUserName = "root"
	 
DBPassword = raw_input("What is the database password>[default : root]")
if not DBPassword :
	DBPassword = "root"

with open('./template-files/Initialization.template', 'r') as init_file:
	data=init_file.read()
	data = data.replace("$UtilsPackage",UtilsPackage)
	data = data.replace("$DBHost",DBHost)
	data = data.replace("$DBDatabase",DBDatabase)
	data = data.replace("$DBUserName",DBUserName)
	data = data.replace("$DBPassword",DBPassword)
	with open("./Generated/"+UtilsPackage+"/Initialization.java",'w') as output_file:
		output_file.write(data)
		output_file.close()
		

AuthEntityName = raw_input("What is the name of the authentication entity[default : User]>")
if not AuthEntityName :
	AuthEntityName = "User"
	
AuthEndpoint = raw_input("What is the name of the endpoint of the authentication entity[default : auth]>")
if not AuthEndpoint :
	AuthEndpoint = "auth"

with open('./template-files/AuthenticationEntity.template', 'r') as init_file:
	data=init_file.read()
	data = data.replace("$EntityPackage",EntityPackage)
	data = data.replace("$AuthEntityName",AuthEntityName)
	with open("./Generated/"+EntityPackage+"/"+AuthEntityName+".java",'w') as output_file:
		output_file.write(data)
		output_file.close()

with open('./template-files/AuthenticationEndpoint.template', 'r') as init_file:
	data=init_file.read()
	data = data.replace("$EntityPackage",EntityPackage)
	data = data.replace("$EndpointPackage",EndpointPackage)
	data = data.replace("$AuthEndpoint",AuthEndpoint)
	data = data.replace("$AuthEntityName",AuthEntityName)
	with open("./Generated/"+EndpointPackage+"/Authentication.java",'w') as output_file:
		output_file.write(data)
		output_file.close()

Next = raw_input("Would you like to generate a management endpoint for accounts?[Y/n]>")
if not Next :
	Next = "Y"
if 'y' in Next or 'Y' in Next :
	ManagementEndpoint = raw_input("What is the path of this endpoint?[default : user]>")
	if not ManagementEndpoint : 
		ManagementEndpoint = "user"
	EntityName = AuthEntityName
	Endpoint = ManagementEndpoint
	EndpointName = AuthEntityName+"Endpoint"
	ListLevel = "2"
	CreateLevel = "-1"
	ReadLevel = "2"
	UpdateLevel = "1"
	DeleteLevel = "2"
	Checking = """
	    if(json.containsKey("level")){
            throw new Authorisation.UnauthorisedException();
        }
        json.put("level",1)//the default user level is one
	"""
	with open('./template-files/AuthenticatedEndpoint.template', 'r') as init_file:
			data = init_file.read()
			data = data.replace("$EndpointPackage",EndpointPackage)
			data = data.replace("$EntityPackage",EntityPackage)
			data = data.replace("$EntityName",EntityName)
			data = data.replace("$Endpoint",Endpoint)
			data = data.replace("$EndpointName",EndpointName)
			data = data.replace("$AuthEntityName",AuthEntityName)	
			data = data.replace("$Checking",Checking)
			data = data.replace("$ListLevel",ListLevel)
			data = data.replace("$CreateLevel",CreateLevel)
			data = data.replace("$ReadLevel",ReadLevel)
			data = data.replace("$UpdateLevel",UpdateLevel)
			data = data.replace("$DeleteLevel",DeleteLevel)
			with open("./Generated/"+EndpointPackage+"/"+EndpointName+".java",'w') as output_file:
				output_file.write(data)
				output_file.close()		
	
   
while True :
	
	try :
		EntityName = raw_input("What is the name of the entity class>")
		EndpointName = raw_input("What is the name of the endpoint class>")
		Endpoint = raw_input("What is the path of the endpoint[eg, 'user/reserve' or 'room']>")
		with open('./template-files/Entity.template', 'r') as init_file:
			EntityParameters = ""
			data=init_file.read()
			data = data.replace("$EntityPackage",EndpointPackage)
			data = data.replace("$EntityName",EntityName)
			with open("./Generated/"+EntityPackage+"/"+EntityName+".java",'w') as output_file:
				output_file.write(data)
				output_file.close()
	
		with open('./template-files/AuthenticatedEndpoint.template', 'r') as init_file:
			data = init_file.read()
			data = data.replace("$EndpointPackage",EndpointPackage)
			data = data.replace("$EntityPackage",EntityPackage)
			data = data.replace("$EntityName",EntityName)
			data = data.replace("$Endpoint",Endpoint)
			data = data.replace("$EndpointName",EndpointName)
			data = data.replace("$AuthEntityName",AuthEntityName)
			data = data.replace("$Checking","")
			ListLevel = raw_input("What is the needed privelege for Listing>")
			CreateLevel = raw_input("What is the needed privelege for Creating>")
			ReadLevel = raw_input("What is the needed privelege for Reading>")
			UpdateLevel = raw_input("What is the needed privelege for Updating>")
			DeleteLevel = raw_input("What is the needed privelege for Deleting>")	
			data = data.replace("$ListLevel",ListLevel)
			data = data.replace("$CreateLevel",CreateLevel)
			data = data.replace("$ReadLevel",ReadLevel)
			data = data.replace("$UpdateLevel",UpdateLevel)
			data = data.replace("$DeleteLevel",DeleteLevel)
			with open("./Generated/"+EndpointPackage+"/"+EndpointName+".java",'w') as output_file:
				output_file.write(data)
				output_file.close()		
			
		while True :
			Next = raw_input("Would you like to create another endpoint and entity?>")
			if 'y' in Next or 'Y' in Next :
				break
			elif 'n' in Next or 'N' in Next :
				x = 1/0
			else :
				print("I did not understand that!")
	except ZeroDivisionError :
		print("Okay! Bye!")
		break;
		
