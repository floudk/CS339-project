#include"server.h"

struct ss{
	string s;
	int q;
};

bool cmp(ss a,ss b){
	return a.q<b.q;
} 
vector<string> str2vectorStr(const string &input){
	vector<string> res;
	string tmp;
	for(auto ch:input){
		if(ch=='\n'){
			res.push_back(tmp);
			tmp.clear();
		}else{
			tmp+=ch;
		}
	}
	return res;
}

int contrast(string sz1,string sz2){
	int i=0,j;
	ss s1[1000],s2[1000];
	string s;
	for(i=0;i<1000;i++){
		s1[i].q=s2[i].q=0;
		s1[i].s=s2[i].s="--";
	}
	i=0;
	int mode;
	int jj1=1,jj2=0;
	vector<string> data1,data2;
	data1= str2vectorStr(sz1);
	data2 = str2vectorStr(sz2);
	for(jj1=1;jj1<data1.size()-1;jj1++){
		int i=0;
		while(data1[jj1][i]!=' ')i++;
		s1[jj1-1].s=data1[jj1].substr(0,i);
		int qi=0;	string sss=data1[jj1].substr(i+1);

		if(sss[0]!='0'){
		for(int j=1;j<sss.length();j++){
			qi=qi*10+sss[j]-48;
		}
		s1[jj1-1].q=qi;
		}
		else s1[jj1-1].q=0;
		
	
	}
	//cout<<"I come here 2\n";
	
	for(jj1=1;jj1<data2.size()-1;jj1++){
		int i=0;
		while(data2[jj1][i]!=' ')i++;
		s2[jj1-1].s=data2[jj1].substr(0,i);
		int qi=0;	string sss=data2[jj1].substr(i+1);
		if(sss[0]!='0'){
			for(int j=1;j<sss.length();j++){
				qi=qi*10+sss[j]-48;
			}
			s2[jj1-1].q=qi;
		}
		else 
			s2[jj1-1].q=0;
	
	}
 /* 
 for(int ki=0;ki<data1.size()-1;ki++){
 	cout<<s1[ki].s<<" "<<s1[ki].q<<endl;
 }
 cout<<endl<<1<<endl;
 for(int ki=0;ki<data2.size()-1;ki++){
 	cout<<s2[ki].s<<" "<<s2[ki].q<<endl;
 }
*/
		
	// cout<<"I am here 3"<<endl;		


//cout<<123<<endl;
	i=0;
// while(getline(cin,s)){
// //	cout<<s<<" "<<i<<endl;
// 	if(s=="")break;
// 	if(i%2==0){
// 		int ij=0;
// 		while(s[ij]!=' ')ij++;
// 		s2[i/2].s=s.substr(0,ij);i++;
// 	}
// 	else {
// 		int qi=0;
// 		for(int j=1;j<s.length();j++){
// 			qi=qi*10+s[j]-48;
// 		}
// 		s2[i/2].q=qi;
// 		i++;
// 	}
	
// }
//	cout<<s2[0].s<<endl<<s2[0].q<<endl;
//	cout<<s2[1].s<<endl<<s2[1].q<<endl;
//	cout<<s2[2].s<<endl<<s2[2].q<<endl;

	int r1=0,r2=0;

	while(s1[r1].s!="--"||s1[r1].q!=0)r1++;
	while(s2[r2].s!="--"||s2[r2].q!=0)r2++;
	r1--;r2--;
	int r11=r1,r22=r2;
//     if(r11==-1||r22==-1){
//         for(int ki=0;ki<data1.size()-2;ki++){
//  	cout<<s1[ki].s<<" "<<s1[ki].q<<endl;
//  }
//  cout<<endl<<1<<endl;
//  for(int ki=0;ki<data2.size()-2;ki++){
//  	cout<<s2[ki].s<<" "<<s2[ki].q<<endl;
//  }

//     }

	sort(s1,s1+r1+1,cmp);
	sort(s2,s2+r2+1,cmp);

	r1=0;r2=0;
	while(s1[r1].q<=65&&(s1[r1].s!="--"||s1[r1].q!=0))r1++;
	while(s2[r2].q<=65&&(s2[r2].s!="--"||s2[r2].q!=0))r2++;
	r1--;r2--;



if(r1<13)r1=min(13,r11);
if(r2<13)r2=min(13,r22);


i=0;
int flag;
double score=0;

//cout<<"3\n";
for(i=0;i<=r1;i++){

	for(int j=0;j<=r22;j++){
		if(s1[i].s==s2[j].s){
			if(abs(s1[i].q-s2[j].q)<4)score+=300;
            else if(abs(s1[i].q-s2[j].q)<15)score+=150;
			else if(abs(s1[i].q-s2[j].q)<20)score+=100;
            else score+=30;
			break;
		}
	}

}
score/=max((r1+1),1);


double score2=0;
for(i=0;i<=r2;i++){

	for(int j=0;j<=r11;j++){
		if(s2[i].s==s1[j].s){
			if(abs(s2[i].q-s1[j].q)<4)score+=300;
            else if(abs(s2[i].q-s1[j].q)<15)score+=150;
			else if(abs(s2[i].q-s1[j].q)<20)score+=100;
            else score+=30;
			break;
		}
	}

}
	score2/=max(1,(r2+1));


	score+=score2;
            

	int ans;
	if(score>=600)ans=1;
	if(score>=200&&score<600)ans=0;
	if(score<200)ans=-1;
	//cout<<"4444444\n";
	return ans;
}
