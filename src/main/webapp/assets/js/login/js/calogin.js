//控件加载
function OCXLoad(type){
	var html="";
	if(type!=""){
		if(type=="sdca"){
			html="<OBJECT ID=\"CASecurityClient\"CLASSID=\"CLSID:F8119DB1-73CB-49F7-8559-2B5EDD869D2A\"	style=\"LEFT: 0px; WIDTH: 1px; TOP: 0px; HEIGHT: 1px\" height=\"1\"	width=\"1\"></OBJECT>";
			$("#CAObj").html(html);
		}else if(type=="hljca"){
			html="<object id=\"HTCOM\" classid=\"CLSID:8c0c8ede-0f76-4f32-b298-ff8a16ee3b7e\"></object>";
			$("#CAObj").html(html);
		}else if(type=="hljca_dq"){
			//<script src="static/js/login/js/jquery.carouselCall.js"></script>			
			html+="<OBJECT id=\"oCAPICOM\" codeBase=\"static/js/login/js/capicom.cab#version=2,0,0,3\" classid=\"clsid:A996E48C-D3DC-4244-89F7-AFA33EC60679\" VIEWASTEXT></OBJECT>";
			html+="<script src=\"static/js/login/js/dqcapi.js\"></script>";
			$("#CAObj").html(html);
		}else if(type=="wfca"){
			//<script src="static/js/login/js/jquery.carouselCall.js"></script>
			html+="<object id=\"JITDSignOcx\" style=\" width:0px; height:0px;\"  classid=\"clsid:06CA9432-D9BD-4867-8475-770B131E1759\"></object> <form method=\"get\" class=\"form-login\" ID=\"LoginForm\" name=\"LoginForm\" action=\"${root!}/web/ca/wf/login\" > <input type=\"hidden\" name=\"plain\" id=\"plain\" /> <input type=\"hidden\" name=\"signtext\" cols=\"50\" rows=\"3\" id=\"signtext\" /> <input type=\"hidden\" name=\"sfz\" id=\"sfz\" /> <input type=\"hidden\" name=\"userid\" id=\"userid\" /></form>";
			$("#CAObj").html(html);
		}
	}	
}

//CA登录JS
function CALogin(type){
	if(type!=""){
		if(type=="sdca"){
			sdcaLogin();
		}else if(type=="hljca"){
			hljcaLogin();
		}else if(type=="hljca_dq"){
			hljdqcaLogin();
		}else if(type=="wfca"){
			beforeSubmit();
		}else{
			$("#messageTip").html("<font color='red'>没有配置CA</font>");
		}
	}else{
		$("#messageTip").html("<font color='red'>没有配置CA</font>");
	}
}
//山东济南项目组CA
function sdcaLogin(){
	var  clientCertID=CASecurityClient.SOF_GetUserList();
	var certText=CASecurityClient.SOF_ExportUserCert(clientCertID);	
	$("#certInfo").val(certText);			
	var selectedValue="0x00000021";	
	var certTypeText=CASecurityClient.SOF_GetCertInfo(certText,parseInt(selectedValue));			
	var part = certTypeText.split(","); 
    var userId="";
	 for ( var i = 0; i < part.length; i++) {
		var par = /^\s+/;
		var strRes = part[i].replace(par, '');
		if (strRes.indexOf("OU=") == 0) {
			//取得OU用户账号
			userId = strRes.substr(3, strRes.length);					
		}
	} 
	if(userId!=""){	
		var cipherText=CASecurityClient.SOF_SignDataByP7(clientCertID,userId);				
		$("#caName").val(userId);
		$("#loginType").val("CA");
		$("#username").val("11");
		$("#password").val("11");
		$("#btnLogin").click();
	}
}
//黑龙江佳木斯CA登录
function hljcaLogin(){
	//输入密码,并验证
	var pswd=prompt("请输入CA证书密码","");	
	if(pswd!=null){
		try{			
			var trynum =HTCOM.HCOMCheckUserPin(0,pswd);			
			if(trynum==-1){//密码正确				
				var SN=HTCOM.getKeySN().split(";")[0];				
				//使用SN去服务端验证，得到用户名
				$("#caName").val(SN);
				$("#loginType").val("CA");
				$("#username").val("11");
				$("#password").val("11");
				$("#btnLogin").click();
			}else{
				alert("U盾密码错误！还剩"+trynum+"次机会!");
			}
		}catch(e){
			//console.log(e);
		}		
	}
}
//黑龙江大庆CA登录
function hljdqcaLogin(){
	if(!hasKeyInserted()){
		alert("未插入USB Key"); 
	}else{
		var check=checkPin();
		if(check){
			var sn=getCertSN().split(";")[0];		
			$("#caName").val(sn);
			$("#loginType").val("CA");
			$("#username").val("11");
			$("#password").val("11");
			$("#btnLogin").click();
		}		
	}
}
function beforeSubmit(){
	if (checkOcxExists()) {
		debugger;
		var plain, signtext, temp_AttachSign_Result;
		plain = $("#plain").val();

		JITDSignOcx.SetCertChooseType(0);
		//证书选择的类型（0 表示只有一张证书时也弹出证书选择框，1 表示只有一张证书时将不弹出证书选择框，默认值为0）
		JITDSignOcx.SetCert("SC", "", "", "", "", "");
		//获取主题
		var sSubject = JITDSignOcx.getCertInfo("SC", 0, "");
		if (sSubject == "") {
			alert("没有检测到USBKEY，请检查KEY是否插入后刷新此页面！");
			return;
		}

		var posNM1 = sSubject.indexOf("CN=", 0);
		var posoui = sSubject.indexOf("OU=I", 0);   //获取身份证  标志为“OU=I”
		var posou = sSubject.indexOf(",", posoui);  //这里以，号做分隔符
		var sfz = sSubject.substring(posoui + 4, posou);  //获取身份证成功

		var posNM2 = sSubject.indexOf(",", posNM1);
		var posID1 = sSubject.indexOf("OU=ID", 0);
		var posID2 = sSubject.indexOf(",", posID1);
		var NM = sSubject.substring(posNM1 + 3, posNM2);
		var ID = sSubject.substring(posID1 + 5, posID2);
		$("#plain").val(ID);
		temp_AttachSign_Result = JITDSignOcx.AttachSign("", ID);
		if (JITDSignOcx.GetErrorCode() != 0) {
			alert("错误码：" + JITDSignOcx.GetErrorCode() + " 错误信息：" + JITDSignOcx.GetErrorMessage());
		} else {
			$("#signtext").val(temp_AttachSign_Result);
			$("#userid").val(ID);//传值到后台判断登录ID
			$("#sfz").val(sfz); //传值到后台判断身份证号
			$('#LoginForm').submit();
		}
	}
}

function checkOcxExists(){
	try{
		//证书选择的类型（0 表示只有一张证书时也弹出证书选择框，1 表示只有一张证书时将不弹出证书选择框，默认值为0）
		JITDSignOcx.SetCertChooseType(1);
	}catch (e){
		alert("客户端安全控件没有安装或安装失败，请安装客户端安全控件后登录。");
		//window.history.go(-1);
		return false;
	}
	return true;
}