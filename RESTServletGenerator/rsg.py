
EntityPackage = raw_input("What is the entity package name :")
EndpointPackage = raw_input("What is the endpoint package name : ")
'''UtilsPackage = raw_input("What is the utils package name : ")
DBHost = raw_input("What is the URL of the database host : ")
DBDatabase = raw_input("What is the name of the database : ")
DBUserName = raw_input("What is the database username : ")
DBPassword = raw_input("What is the database password : ")

with open('./template-files/Initialization.template', 'r') as init_file:
    data=init_file.read()
    data = data.replace("$UtilsPackage",UtilsPackage)
    data = data.replace("$DBHost",DBHost)
    data = data.replace("$DBDatabase",DBDatabase)
    data = data.replace("$DBUserName",DBUserName)
    data = data.replace("$DBPassword",DBPassword)
    print(data)
'''
AuthEntityName = raw_input("What is the name of the authentication entity : ")
AuthEndpoint = raw_input("What is the name of the endpoint of the authentication entity : ")

with open('./template-files/AuthenticationEntity.template', 'r') as init_file:
    data=init_file.read()
    data = data.replace("$EntityPackage",EntityPackage)
    data = data.replace("$AuthEntityName",AuthEntityName)
    print(data)

with open('./template-files/AuthenticationEndpoint.template', 'r') as init_file:
    data=init_file.read()
    data = data.replace("$EndpointPackage",EndpointPackage)
    data = data.replace("$AuthEndpoint",AuthEndpoint)
    print(data)
