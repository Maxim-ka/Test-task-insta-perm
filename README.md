## Test-task-insta-perm

Create two pages:  
- 1 page) Authorization, where there are two fields - Partners Login and Password
- 2 page) List of signals by instruments: EURUSD, GBPUSD, USDJPY, USDCHF, USDCAD, AUDUSD, NZDUSD

Partners account for your testing:  
Login: 20234561  
Password: ladevi31

1) Authorization:

http://client-api.instaforex.com/Home/GetAPIUsageInfo  
You need to get token "RequestMoblieCabinetApiToken".

Request URL: http://client-api.instaforex.com/api/Authentication/RequestMoblieCabinetApiToken    
Method: POST  
Request:  
{  
"Login": "PARTNERS_LOGIN",  
"Password": "PARTNERS_PASSWORD"  
}  

In response you get "passkey" (your token).

2) Get list of signals by instruments: EURUSD, GBPUSD, USDJPY, USDCHF, USDCAD, AUDUSD, NZDUSD

Request URL: http://client-api.instaforex.com/clientmobile/GetAnalyticSignals/PARTNERS_LOGIN?tradingsystem=3&pairs=EURUSD,GBPUSD,USDJPY,USDCHF,USDCAD,AUDUSD,NZDUSD&from=UNIX_DATE&to=UNIX_DATE  
Example: http://client-api.instaforex.com/clientmobile/GetAnalyticSignals/7777777?tradingsystem=3&pairs=GBPJPY,EURJPY&from=1479860023&to=1480066860  
Method: GET   

You need add HTTP header "passkey" with security token value to request.
passkey: your token

Successful response example:

{  
"Id":16760,  
"ActualTime":1520217807,  
"Comment":"Trend channel",  
"Pair":"AUDUSD",  
"Cmd":5,  
"TradingSystem":3,  
"Period":"H1",  
"Price":0.77648,  
"Sl":0.77846,  
"Tp":0.7745  
}  