package ph.sinonet.vg.live.utils;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.EnvironmentStringPBEConfig;
import org.jasypt.spring.properties.EncryptablePropertyPlaceholderConfigurer;
import org.jasypt.util.text.BasicTextEncryptor;

public class SpecialEnvironmentStringPBEConfig extends EnvironmentStringPBEConfig {

	@Override
	public void setAlgorithm(String algorithm) {
		super.setAlgorithm("PBEWithMD5AndDES");
	}

	@Override
	public void setPassword(String password) {
		super.setPassword("asdf737!23adf%jc");
	}

	public SpecialEnvironmentStringPBEConfig() {
		setAlgorithm("PBEWithMD5AndDES");
		setPassword("asdf737!23adf%jc");
	}

	public static void main(String[] args) {
		SpecialEnvironmentStringPBEConfig sspbe = new SpecialEnvironmentStringPBEConfig();
		StandardPBEStringEncryptor spbe = new StandardPBEStringEncryptor();
		spbe.setConfig(sspbe);

		EncryptablePropertyPlaceholderConfigurer enc = new EncryptablePropertyPlaceholderConfigurer(spbe);


//		//加密
//		BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
//		textEncryptor.setPassword("ifewfj392");
//		String newPassword = textEncryptor.encrypt("jdbc:mysql://120.28.15.17:3306/oasystem?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull");
//		System.out.println(newPassword);
//		//解密
//		BasicTextEncryptor textEncryptor2 = new BasicTextEncryptor();
//		textEncryptor2.setPassword("jiaoba");
//		String oldPassword = textEncryptor2.decrypt("LOCIDd6rH1pk9Ox5ZQ2zjg==");
//		System.out.println(oldPassword);

	}
}
