var CAPICOM_STORE_OPEN_READ_ONLY = 0;
var CAPICOM_CURRENT_USER_STORE = 2;
var CAPICOM_CERTIFICATE_FIND_SHA1_HASH = 0;
var CAPICOM_CERTIFICATE_FIND_EXTENDED_PROPERTY = 6;
var CAPICOM_CERTIFICATE_FIND_TIME_VALID = 9;
var CAPICOM_CERTIFICATE_FIND_KEY_USAGE = 12;
var CAPICOM_DIGITAL_SIGNATURE_KEY_USAGE = 0x00000080;
var CAPICOM_AUTHENTICATED_ATTRIBUTE_SIGNING_TIME = 0;
var CAPICOM_INFO_SUBJECT_SIMPLE_NAME = 0;
var CAPICOM_ENCODE_BASE64 = 0;
var CAPICOM_E_CANCELLED = -2138568446;
var CERT_KEY_SPEC_PROP_ID = 6;

/** * 
 *  判断CAPICOM是否安装 * 
 */
function IsCAPICOMInstalled() {
	if (typeof (oCAPICOM) == "object") {
		if ((oCAPICOM.object != null)) {
			// We found CAPICOM!
			return true;
		}
	}
	return false;
}
/** * 
 *  取得签名证书 * 
 */
function getSignCertificate() {
	var MyStore = new ActiveXObject("CAPICOM.Store");
	try {
		MyStore.Open(CAPICOM_CURRENT_USER_STORE, "My",
				CAPICOM_STORE_OPEN_READ_ONLY);
	} catch (e) {
		alert("不能打开证书。");
		return false;
	}
	var signCert;
	for ( var i = 1; i <= MyStore.Certificates.Count; i++) {
		var certificate = MyStore.Certificates(i);
		if (is_CEGN_OCA_ISSUE(certificate)
				&& isDigitalSignCertificate(certificate)) {
			signCert = certificate;
			break;
		}
	}
	return signCert;
}

/** * 
 *  取得证书序列号 * 
 */
function getCertSN() {
	if (!hasKeyInserted()) {
		alert("未找到数字签名证书。");
		return "";
	}
	var cert = getSignCertificate();
	return cert.SerialNumber;
}
/**
 * 
 *  判断是不是CEGN_OCA签发的证书
 * 
 */
function is_CEGN_OCA_ISSUE(certificate) {
	var issueName = certificate.IssuerName;
	return issueName.indexOf("CEGN_OCA") >= 0;
}
/**
 * 
 *  判断是不是数字签名的证书
 * 
 */
function isDigitalSignCertificate(certificate) {
	var isSignCert = false;

	for ( var i = 1; i <= certificate.Extensions().Count; i++) {
		var extName = certificate.Extensions().Item(i).OID.FriendlyName;
		var extData = certificate.Extensions().Item(i).EncodedData.Format(true);

		if ((extName.indexOf("密钥用法") >= 0 || extName.indexOf("Key Usage") >= 0)
				&& extData.indexOf("Digital Signature") >= 0) {
			isSignCert = true;
			break;
		}
	}
	return isSignCert;
}

/**
 * 
 *  判断是否插入usb key
 * 
 */
function hasKeyInserted() {
	var ftkeyCnt = 0;
	var cert = getSignCertificate();
	if (cert != null) {
		ftkeyCnt = 1;
	}
	return ftkeyCnt > 0;
}
/**
 * 
 *  取得加密证书
 * 
 */
function getEnciphermentCertificate() {
    var MyStore = new ActiveXObject("CAPICOM.Store");
    try
    {
        MyStore.Open(CAPICOM_CURRENT_USER_STORE, "My", CAPICOM_STORE_OPEN_READ_ONLY);
    }
    catch (e)
    {
        alert("不能打开证书。");
        return false;
    }

    var signCert;
    for(var i = 1; i<= MyStore.Certificates.Count; i++) {
        var certificate = MyStore.Certificates(i);
        


        if (is_CEGN_OCA_ISSUE(certificate) && isEnciphermentCertificate(certificate)) {
            signCert = certificate;
            break;
        }
    }

    return signCert;
}
/**
 * 
 *  判断是不是加密的证书
 * 
 */
function isEnciphermentCertificate(certificate) {
    var isEnciphermentCert = false;
    
    for (var i = 1; i <= certificate.Extensions().Count; i++) {
        var extName = certificate.Extensions().Item(i).OID.FriendlyName;
        var extData = certificate.Extensions().Item(i).EncodedData.Format(true);

        
        if ((extName.indexOf("密钥用法")>=0 || extName.indexOf("Key Usage")>=0) && extData.indexOf("Key Encipherment")>=0) {
            isEnciphermentCert = true;
            break;
        }
    }
    

    return isEnciphermentCert;
}
//检查pin码
function checkPin() {
    // instantiate the CAPICOM objects
    var SignedData = new ActiveXObject("CAPICOM.SignedData");
    var Signer = new ActiveXObject("CAPICOM.Signer");
    var TimeAttribute = new ActiveXObject("CAPICOM.Attribute");

    var cert = getEnciphermentCertificate();
    // only do this if the user selected a certificate
    if (cert.Thumbprint != "") {

        // Set the data that we want to sign
        SignedData.Content = "data";
        try
        {
            // Set the Certificate we would like to sign with
            Signer.Certificate = cert;
            
            // Set the time in which we are applying the signature
            var Today = new Date();
            TimeAttribute.Name = CAPICOM_AUTHENTICATED_ATTRIBUTE_SIGNING_TIME;
            TimeAttribute.Value = Today.getVarDate();
            Today = null;
            Signer.AuthenticatedAttributes.Add(TimeAttribute);
            
            // Do the Sign operation
            var szSignature = SignedData.Sign(Signer, true, CAPICOM_ENCODE_BASE64);    

            return szSignature != "";
        }
        catch (e)
        {
            if (e.number != CAPICOM_E_CANCELLED)
            {
                //alert("An error occurred when attempting to sign the content, the errot was: " + e.description +e.number);
                var e1;
                if(e.number<0)
                    e1=e.number+0x7fffffff+0x7fffffff+2;
                else
                    e1=e.number;
                //alert("error number:0x"+e1.toString(16));
                return false;
            }
        }
    }
}