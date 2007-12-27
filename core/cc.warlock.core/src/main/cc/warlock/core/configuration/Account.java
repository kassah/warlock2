package cc.warlock.core.configuration;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.ArrayList;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import org.apache.commons.codec.binary.Base64;

public class Account {

	protected String accountName, password;
	protected ArrayList<Profile> profiles = new ArrayList<Profile>();
	
	public static String decryptPassword (String encrypted)
	{
		DESEncrypter encrypter = new DESEncrypter("$warlockpassword$");
		return encrypter.decrypt(encrypted);
	}
	
	public static String encryptPassword (String password)
	{
		DESEncrypter encrypter = new DESEncrypter("$warlockpassword$");
		return encrypter.encrypt(password);
	}
	
	protected static class DESEncrypter {
		protected Cipher eCipher, dCipher;
		protected byte[] salt = {
			(byte)0x98, (byte)0x93, (byte)0xA7, (byte)0x44,
			(byte)0x32, (byte)0x12, (byte)0x34, (byte)0xF3
		};
		protected int iterations = 12;
		
		public DESEncrypter (String password)
		{
			try {
				KeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt, iterations);
				SecretKey key = SecretKeyFactory.getInstance(
					"PBEWithMD5AndDES").generateSecret(keySpec);
				eCipher = Cipher.getInstance(key.getAlgorithm());
				dCipher = Cipher.getInstance(key.getAlgorithm());
				
				AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, iterations);
				
				eCipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
				dCipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
			} catch (InvalidKeyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidKeySpecException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchPaddingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidAlgorithmParameterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		public String encrypt (String text)
		{
			try {
				byte[] utf8 = text.getBytes("UTF8");
				byte[] encoded = eCipher.doFinal(utf8);
				return new String(Base64.encodeBase64(encoded));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalBlockSizeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (BadPaddingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		
		public String decrypt (String text)
		{
			try {
				byte[] encoded = Base64.decodeBase64(text.getBytes());
				byte[] utf8 = dCipher.doFinal(encoded);
				
				return new String(utf8, "UTF8");
			} catch (IllegalBlockSizeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (BadPaddingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
	}
	
	public Account () { }
	public Account (String name, String password)
	{
		this.accountName = name;
		this.password = password;
	}
	
	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof String)
		{
			return accountName.equals(obj);
		}
		
		return super.equals(obj);
	}
	
	@Override
	public int hashCode() {
		return accountName.hashCode();
	}

	public ArrayList<Profile> getProfiles() {
		return profiles;
	}
}
