#include"server.h"

string decodeid(string s){
	struct tm *local; 
	time_t t; 
	t=time(NULL); 
	local=localtime(&t);
	int month=local->tm_mon+1;
	int day=local->tm_mday;
//	if(mon>=10)mm=to_string(mon);
//	string mm=(mon<10?"0"+to_string(mon):to_string(mon));
//	string dd=(day<10?"0"+to_string(day):to_string(day));
//	string tt=mm+dd;
//	cout<<tt;
//	int ui=tt[3]-48;
//	int a0=tt[0]-48,a1=tt[1]-48,a2=tt[2]-48;
	
	if (s[0] <= 57 && s[0] >= 48) {
            while (month >= 10)
                month -= 10;
            s[0] -= month;
            while (s[0] <48)
                s[0] += 10;
        } else {
        	s[0]-=52;
            s[0] += month;
            while (s[0] < 97)
                s[0] += 26;
        }
        if (s[1] <= 57 && s[1] >= 48) {
            while (day >= 10)
                day -= 10;
            s[1] -= day;
            while (s[1] <48)
                s[1] += 10;
        } else {
            s[1] -= 52;
            s[1]+=day;
            while (s[1] < 97)
                s[1] += 26;
        }

return s;
}
