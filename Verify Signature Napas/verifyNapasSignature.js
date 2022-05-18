//Khai báo js lib sign và verify theo JWS
const jose = require('jose');

const fs = require('fs');
//Khai báo Js lib lấy giá trị trên header request
var hm = require('header-metadata');
//Khai báo thuật toán ký
const alg = new Buffer('{"alg":"RS256"}','utf8');
//Khai bao certname
//var publicKey='SEATECH_SCERT';
const mpgwUtil = require('local:///API_UTIL/MpgwUtil.js');
var crypto = require('crypto');

/* =================================================================================================================================
Verify basic rsa256 signature using crypto library for NAPAS intergration
public key duoc import vao database name=napas-cert
================================================================================================================================= */  
//Hàm Convert String public certificate thành định dạng Pem file;
console.debug("--------------------- Begin validate signature ---------------------------------------");
function convertStringToPem(str) {
	str = str.replace(/\n/g, "");
    var pemText = '';
    while(str.length > 0) {
        pemText += str.substring(0, 64) + '\n';
        str = str.substring(64);
    }
	pemText=pemText.replace("-----END PUBLIC KEY-----","");
	pemText=pemText.replace("-----BEGIN PUBLIC KEY-----","");
	var pemText = "-----BEGIN CERTIFICATE-----\n" + pemText + "-----END CERTIFICATE-----";
    return pemText;
}
//get client IP from header
function getClientIP(hm){
	var clientIpArrStr = '';
	var clientIp='';

	console.debug('clientIpArrStr: '+clientIpArrStr);
	// Get all infomation for validate signature
	try{
		clientIpArrStr = mpgwUtil.decectHeaderValue(hm.current.headers,'X-Client-IP');
		clientIp=clientIpArrStr;
		var arrIp=clientIpArrStr.split(",");
		clientIp=arrIp[0];
			
	}catch(e) {
		clientIp=".";
		console.error('cannot get IP');
		console.error(e);
	}		
	return clientIp;
}
//Get signature from napas request
function getSignatureFromBody(jsonBody){
	var requestSignature='';
	try{
		var requestSignature=jsonBody.header.signature;
		console.debug('requestSignature='+requestSignature);
	}catch(e) {
		requestSignature=null;
		console.error('cannot get signature');
		console.error(e);
	}
	return requestSignature;
	
}
function getConfigCertificate(){
	var napasConfigCertificate='';
	fs.readAsJSON("local:///API_UTIL/properties/napas-api-properties.json", function(errorReadFile,data) {
		if (errorReadFile) {
					// Handle the error.
			var locate = 'temporary:///API_LOGs/mpgw-runtime-error-'+mpgwUtil.getDateTime('dd-mm-yyyy')+'.log';
			var strError = '['+mpgwUtil.getDateTime('dd-mm-yyyy hh24:mi:ss')+' ERROR] MessageSecurity_MPGW.MessageSecurity_Policy_DigiSign Cannot read properties file '+errorReadFile+'\r\n';
			mpgwUtil.writeErrorLog(strError,locate,86400);
			console.error('cannot read'+strError);
		} else {
			console.debug(JSON.stringify(data));
			napasConfigCertificate=data.properties.jws.napasCert;
			console.debug(JSON.stringify(napasConfigCertificate));
			
		}
	});
	
	console.debug('napasConfigCertificate='+JSON.stringify(napasConfigCertificate));
	return napasConfigCertificate;
}
//Get payload from napas request
function getPayloadFromRequest(jsonBody){
	var payloadInRequest='{}';
	try{
		var payloadInRequest=jsonBody.payload;
		console.debug('payloadInRequest='+JSON.stringify(payloadInRequest));
	}catch(e) {
		payloadInRequest=null;
		console.error('cannot get payload');
		console.error(e);
	}
	return JSON.stringify(payloadInRequest);	
}
//str is reqyuest body
session.input.readAsBuffer(function(error, str) {
	if (error) {
      // an error occurred when parsing the content, e.g. invalid JSON object
      // uncatched error will stop the processing and the error will be logged
      throw error;
    }
	try{    
		fs.readAsJSON("local:///API_UTIL/properties/napas-api-properties.json", function(errorReadFile,data) {
			if (errorReadFile) {
						// Handle the error.
				var locate = 'temporary:///API_LOGs/mpgw-runtime-error-'+mpgwUtil.getDateTime('dd-mm-yyyy')+'.log';
				var strError = '['+mpgwUtil.getDateTime('dd-mm-yyyy hh24:mi:ss')+' ERROR] MessageSecurity_MPGW.MessageSecurity_Policy_DigiSign Cannot read properties file '+errorReadFile+'\r\n';
				mpgwUtil.writeErrorLog(strError,locate,86400);
				console.error('cannot read'+strError);
			} else {
				console.debug(JSON.stringify(data));
				var ctx = session.name('rblContext') || session.createContext('rblContext');
				ctx.setVariable('clientHeader', hm.current.headers);
				console.debug('str'+str);

				var jsonRequestBody=JSON.parse(str);
				console.debug('str='+jsonRequestBody);
				var payloadInRequest=getPayloadFromRequest(jsonRequestBody);

				var clientIp=getClientIP(hm);
				var signature=getSignatureFromBody(jsonRequestBody);
				console.debug('signature OUT='+JSON.stringify(signature));
				console.debug('payloadInRequest OUT='+JSON.stringify(payloadInRequest));
				var certificate = '';//""+jwk.toBuffer(certificate);
				certificate=data.properties.jws.napasCert;
				if(signature=='null'|| signature==null||signature==''){
					hm.response.statusCode = '406 Not Acceptable';
						session.output.write({
							"httpCode": "406",
							"httpMessage": "Non-authoritative Information",
							"moreInformation": "Can't find element Signature in request",
							"locate": "request.header.signature"
					});
					
				}
				else{
					//Create rsa 256 verififer
					var verify = crypto.createVerify('rsa-sha256');
					//Update payload
					verify.update(payloadInRequest);
					if(verify==null){
						hm.response.statusCode = '406 Not Acceptable';
							session.output.write({
								"httpCode": "406",
								"httpMessage": "Non-authoritative Information",
								"moreInformation": "Signature or certificate invalid",
								"locate": "Partner Certificate configuration"
						});	
					}else{
						//verify signature
						console.debug('======================BEGIN VERIFY RSA256 SIGNATURE ==================================== ');
						verify.verify(certificate, signature, function(error) {
							if (error) {
							//Verify Error
								if(error.errorMessage =='*Fail to deserialize the key object*' ){
									hm.response.statusCode = '406 Not Acceptable';
									session.output.write({
									   "httpCode": "406",
									   "httpMessage": "Non-authoritative Information",
									   "moreInformation": "Can't find element partner certificate in API configuration or certificate invalid",
									   "locate": "Partner Certificate configuration"
									});
								}
								if(error.errorMessage =='*Decode signature failed*'){
									hm.response.statusCode = '406 Not Acceptable';
									session.output.write({
									   "httpCode": "406",
									   "httpMessage": "Non-authoritative Information",
									   "moreInformation": "Signature is not valid",
									   "locate": "request.header.signature"
									});
								}
								if(error.errorMessage =='*RSA signature did not verify*'){
									hm.response.statusCode = '406 Not Acceptable';
									session.output.write({
									   "httpCode": "406",
									   "httpMessage": "Non-authoritative Information",
									   "moreInformation": "The request content has changed",
									   "locate": "request.body"
									});
								}
								//session.reject('Invalid public key');
								console.error(error.errorMessage);
								return;
							} else {
							// All signature verifications have succeeded
							// therefore payload may be trusted
							//Verify success
								console.debug('======================END VERIFY RSA256 SIGNATURE SUCCESS ==================================== ');

								hm.response.set('Validate-signature-status', 'Sucess');
								hm.response.set('X-Client-IP', clientIp);
								var thePlainText =  str;
								session.output.write(thePlainText);
							}
						});
					}
				}
			}
		});	

	//session.output.write(a);
    }catch(e){
        console.error(e);
        hm.response.statusCode = '406 Not Acceptable';
        session.output.write({
            "httpCode": "406 Not Acceptable",
            "httpMessage": "Exception in signature validation phase",
            "moreInformation": "Exception in signature validation phase.",
            "locate": "null"
        });
    }
});