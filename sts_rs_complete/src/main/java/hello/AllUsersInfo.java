/**
 * 
 */
package hello;

import java.util.List;
import java.util.Map;

/**
 * @author Varun
 *
 */
public class AllUsersInfo {
	
	Map<String, List<IdCard>> idCardMap;
	Map<String, List<WebLogin>> webLoginMap;
	Map<String, List<BankAccount>> bankAccountMap;
	
	/**
	 * Default Constructor
	 */
	public AllUsersInfo(){
    	
    }

	/**
	 * @return the idCardMap
	 */
	public Map<String, List<IdCard>> getIdCardMap() {
		return idCardMap;
	}

	/**
	 * @param idCardMap the idCardMap to set
	 */
	public void setIdCardMap(Map<String, List<IdCard>> idCardMap) {
		this.idCardMap = idCardMap;
	}

	/**
	 * @return the webLoginMap
	 */
	public Map<String, List<WebLogin>> getWebLoginMap() {
		return webLoginMap;
	}

	/**
	 * @param webLoginMap the webLoginMap to set
	 */
	public void setWebLoginMap(Map<String, List<WebLogin>> webLoginMap) {
		this.webLoginMap = webLoginMap;
	}

	/**
	 * @return the bankAccountMap
	 */
	public Map<String, List<BankAccount>> getBankAccountMap() {
		return bankAccountMap;
	}

	/**
	 * @param bankAccountMap the bankAccountMap to set
	 */
	public void setBankAccountMap(Map<String, List<BankAccount>> bankAccountMap) {
		this.bankAccountMap = bankAccountMap;
	}

}
