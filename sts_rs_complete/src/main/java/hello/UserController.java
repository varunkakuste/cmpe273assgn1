package hello;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author Varun
 * User Controller class
 */
@RestController
public class UserController extends WebMvcConfigurerAdapter {
	AllUsersInfo allUsersInfo = new AllUsersInfo();
	final AtomicLong userId = new AtomicLong();
	private final AtomicLong idCardLong = new AtomicLong();
	private final AtomicLong webLoginLong = new AtomicLong();
	private final AtomicLong bankAccLong = new AtomicLong();
	Map<String, User> userMap = new HashMap<String, User>();
	Map<String, List<IdCard>> createdIdCards = new HashMap<String, List<IdCard>>();
	Map<String, List<WebLogin>> createdWebLoginIds = new HashMap<String, List<WebLogin>>();
	Map<String, List<BankAccount>> createdBankAcc = new HashMap<String, List<BankAccount>>();
	SimpleDateFormat sdf = null;
	Date date = null;
	String emptyString = "";

    /**
     * Create User
     * @param user
     * @return
     */
	@RequestMapping(value="/users", method=RequestMethod.POST, consumes="application/json")
    public ResponseEntity<User> createUser(@RequestBody @Valid User user, String user_id, BindingResult bindingResult) {
    	
    	ResponseEntity<User> respEntity = null;
    	
    	if (bindingResult.hasErrors()) {
    		respEntity = new ResponseEntity<User>(user, HttpStatus.BAD_REQUEST);
        } else {
	       	Long newUserId = userId.incrementAndGet();
	    	String newUserIdStr = "u-" + newUserId;
	    	date = new Date();
	    	sdf = new SimpleDateFormat("MM/dd/yyyy h:mm:ss a");
	    	user.setCreated_at(sdf.format(date));
	    	
	    	User newUser = new User(newUserIdStr, chkIsNull(user.getEmail()), chkIsNull(user.getPassword()), 
	    			chkIsNull(user.getName()), chkIsNull(user.getCreated_at()), chkIsNull(user.getUpdated_at()));
	    	userMap.put(newUserIdStr, newUser);
	    	
	    	respEntity = new ResponseEntity<User>(newUser, HttpStatus.CREATED);
        }
    	return respEntity;
    }
    
    /**
     * View User
     * @param user_id
     * @return
     */
    @RequestMapping(value="/users/{user_id}", method=RequestMethod.GET, produces="application/json")
    public ResponseEntity<User> viewUser(@PathVariable String user_id) {
    	User user = null;
    	ResponseEntity<User> respEntity = null;
    	
    	if(userMap != null && userMap.containsKey(user_id)) {
    		user = userMap.get(user_id);
    		respEntity = new ResponseEntity<User>(user, HttpStatus.OK);
    	} else {
    		user = new User();
    		respEntity = new ResponseEntity<User>(user, HttpStatus.NO_CONTENT);
    	}
    	
    	return respEntity;
    }
    
    /**
     * Update User
     * @param user
     * @param user_id
     * @return
     */
    @RequestMapping(value="/users/{user_id}", method=RequestMethod.PUT)
    public ResponseEntity<User> updateUser(@RequestBody @Valid User user, @PathVariable String user_id, BindingResult bindingResult) {
    	date = new Date();
    	sdf = new SimpleDateFormat("MM/dd/yyyy h:mm:ss a");
    	User getExistingUser = null;
    	ResponseEntity<User> respEntity = null;
    	
    	if (bindingResult.hasErrors()) {
    		respEntity = new ResponseEntity<User>(user, HttpStatus.BAD_REQUEST);
        } else {
	    	if(userMap != null && userMap.containsKey(user_id)) {
	    		getExistingUser = userMap.get(user_id);
				getExistingUser.setEmail(user.getEmail());
				getExistingUser.setPassword(user.getPassword());
				getExistingUser.setName(user.getName());
	        	getExistingUser.setUpdated_at(sdf.format(date));
	    		respEntity = new ResponseEntity<User>(getExistingUser, HttpStatus.CREATED);
	    	} else {
	    		getExistingUser = new User();
	    		respEntity = new ResponseEntity<User>(getExistingUser, HttpStatus.NO_CONTENT);
	    	}
        }
    	return respEntity;
    }
    
    /**
     * Create ID Card
     * @param idCard
     * @param user_id
     * @return
     */
    @RequestMapping(value="/users/{user_id}/idcards", method=RequestMethod.POST, consumes="application/json")
    public ResponseEntity<IdCard> createIdCard(@RequestBody @Valid IdCard idCard, @PathVariable String user_id, BindingResult bindingResult) {
    	List<IdCard> idCardLst = new ArrayList<IdCard>();
    	
    	ResponseEntity<IdCard> respEntity = null;
    	
    	if (bindingResult.hasErrors()) {
    		respEntity = new ResponseEntity<IdCard>(idCard, HttpStatus.BAD_REQUEST);
        } else {
	    	Long newCardId = idCardLong.incrementAndGet();
	    	String newCardIdStr = "c-" + newCardId;
	    	
	    	if(allUsersInfo != null && allUsersInfo.getIdCardMap() != null && allUsersInfo.getIdCardMap().containsKey(user_id)) {
	    		idCardLst = allUsersInfo.getIdCardMap().get(user_id);
	    	}
	
	    	IdCard newIdCard = new IdCard(newCardIdStr, idCard.getCard_name(), idCard.getCard_number(), 
								idCard.getExpiration_date());
	    	
	    	idCardLst.add(newIdCard);
	    	createdIdCards.put(user_id, idCardLst);
	    	allUsersInfo.setIdCardMap(createdIdCards);
	    	respEntity = new ResponseEntity<IdCard>(newIdCard, HttpStatus.CREATED);
        }
    	return respEntity;
    }
    
    /**
     * List All ID Cards
     * @param user_id
     * @return
     */
    @RequestMapping(value="/users/{user_id}/idcards", method=RequestMethod.GET, produces="application/json")
    public ResponseEntity<List<IdCard>> listAllIdCard(@PathVariable String user_id) {
    	List<IdCard> idCardsList = null;
    	ResponseEntity<List<IdCard>> respEntity = null;
    	if(allUsersInfo != null && allUsersInfo.getIdCardMap() != null && allUsersInfo.getIdCardMap().containsKey(user_id)) {
    		idCardsList = allUsersInfo.getIdCardMap().get(user_id);
    		respEntity = new ResponseEntity<List<IdCard>>(idCardsList, HttpStatus.OK);
    	} else {
    		idCardsList = new ArrayList<IdCard>();
    		respEntity = new ResponseEntity<List<IdCard>>(idCardsList, HttpStatus.NO_CONTENT);
    	}
    	return respEntity;
    }
    
    /**
     * Delete ID Card
     * @param user_id
     * @param card_id
     * @return
     */
    @RequestMapping(value="/users/{user_id}/idcards/{card_id}", method=RequestMethod.DELETE)
    public ResponseEntity<List<IdCard>> deleteIdCard(@PathVariable String user_id, @PathVariable String card_id) {
    	List<IdCard> idCardLst = new ArrayList<IdCard>();
    	if(allUsersInfo != null && allUsersInfo.getIdCardMap() != null && allUsersInfo.getIdCardMap().containsKey(user_id)) {
    		idCardLst = allUsersInfo.getIdCardMap().get(user_id);
			for(IdCard idCard : idCardLst) {
	    		if(card_id != null && card_id.equalsIgnoreCase(idCard.getCard_id())) {
	    			idCardLst.remove(idCard);
	    			break;
	    		}
	    	}
    	}
		return new ResponseEntity<List<IdCard>>(idCardLst, HttpStatus.NO_CONTENT);  
    }

    /**
     * Create Web Login
     * @param webLogin
     * @param user_id
     * @return
     */
    @RequestMapping(value="/users/{user_id}/weblogins", method=RequestMethod.POST, consumes="application/json")
    public ResponseEntity<WebLogin> createWebLogin(@RequestBody @Valid WebLogin webLogin, @PathVariable String user_id, BindingResult bindingResult) {
    	List<WebLogin> webLoginLst = new ArrayList<WebLogin>();
    	ResponseEntity<WebLogin> respEntity = null;
    	
    	if (bindingResult.hasErrors()) {
    		respEntity = new ResponseEntity<WebLogin>(webLogin, HttpStatus.BAD_REQUEST);
        } else {
	    	Long loginId = webLoginLong.incrementAndGet();
	    	String loginIdStr = "l-" + loginId;
	    	
	    	if(allUsersInfo != null && allUsersInfo.getWebLoginMap() != null && allUsersInfo.getWebLoginMap().containsKey(user_id)) {
	    		webLoginLst = allUsersInfo.getWebLoginMap().get(user_id);
	    	}
	
	    	WebLogin newWebLogin = new WebLogin(loginIdStr, webLogin.getUrl(), webLogin.getLogin(), 
								webLogin.getPassword());
	    	
	    	webLoginLst.add(newWebLogin);
	    	createdWebLoginIds.put(user_id, webLoginLst);
	    	allUsersInfo.setWebLoginMap(createdWebLoginIds);
	    	respEntity = new ResponseEntity<WebLogin>(newWebLogin, HttpStatus.CREATED);  
        }
    	return respEntity;
    }
    
    /**
     * List All Web-site Logins
     * @param user_id
     * @return
     */
    @RequestMapping(value="/users/{user_id}/weblogins", method=RequestMethod.GET, produces="application/json")
    public ResponseEntity<List<WebLogin>> listAllWebLogins(@PathVariable String user_id) {
    	List<WebLogin> webLoginList = null;
    	ResponseEntity<List<WebLogin>> respEntity = null;
    	
    	if(allUsersInfo != null && allUsersInfo.getWebLoginMap() != null && allUsersInfo.getWebLoginMap().containsKey(user_id)) {
    		webLoginList = allUsersInfo.getWebLoginMap().get(user_id);
    		respEntity = new ResponseEntity<List<WebLogin>>(webLoginList, HttpStatus.OK);
    	} else {
    		webLoginList = new ArrayList<WebLogin>();
    		respEntity = new ResponseEntity<List<WebLogin>>(webLoginList, HttpStatus.NO_CONTENT);
    	}
    	
    	return respEntity;
    }
    
    /**
     * Delete Web Login
     * @param user_id
     * @param login_id
     * @return
     */
    @RequestMapping(value="/users/{user_id}/weblogins/{login_id}", method=RequestMethod.DELETE)
    public ResponseEntity<List<WebLogin>> deleteWebLogin(@PathVariable String user_id, @PathVariable String login_id) {
    	List<WebLogin> webLoginList = new ArrayList<WebLogin>();
    	
    	if(allUsersInfo != null && allUsersInfo.getWebLoginMap() != null && allUsersInfo.getWebLoginMap().containsKey(user_id)) {
    		webLoginList = allUsersInfo.getWebLoginMap().get(user_id);
			for(WebLogin webLogin : webLoginList) {
	    		if(login_id != null && login_id.equalsIgnoreCase(webLogin.getLogin_id())) {
	    			webLoginList.remove(webLogin);
	    			break;
	    		}
	    	}
    	}
    	
		return new ResponseEntity<List<WebLogin>>(webLoginList, HttpStatus.NO_CONTENT);  
    }
    
    /**
     * Create Bank Account
     * @param bankAcc
     * @param user_id
     * @return
     */
    @RequestMapping(value="/users/{user_id}/bankaccounts", method=RequestMethod.POST, consumes="application/json")
    public ResponseEntity<BankAccount> createBankAccount(@RequestBody @Valid BankAccount bankAcc, @PathVariable String user_id, BindingResult bindingResult) {
    	List<BankAccount> bankAccLst = new ArrayList<BankAccount>();
    	ResponseEntity<BankAccount> respEntity = null;
    	
    	if (bindingResult.hasErrors()) {
    		respEntity = new ResponseEntity<BankAccount>(bankAcc, HttpStatus.BAD_REQUEST);
        } else {
	    	Long bankAccId = bankAccLong.incrementAndGet();
	    	String bankAccIdStr = "b-" + bankAccId;
	    	
	    	if(allUsersInfo != null && allUsersInfo.getBankAccountMap() != null && allUsersInfo.getBankAccountMap().containsKey(user_id)) {
	    		bankAccLst = allUsersInfo.getBankAccountMap().get(user_id);
	    	}
	
	    	BankAccount newBankAcc = new BankAccount(bankAccIdStr, bankAcc.getAccount_name(), bankAcc.getRouting_number(), 
								bankAcc.getAccount_number());
	    	
	    	bankAccLst.add(newBankAcc);
	    	createdBankAcc.put(user_id, bankAccLst);
	    	allUsersInfo.setBankAccountMap(createdBankAcc);
	    	respEntity = new ResponseEntity<BankAccount>(newBankAcc, HttpStatus.CREATED);  
        }
    	return respEntity;
    }

    /**
     * List All Bank Accounts
     * @param user_id
     * @return
     */
    @RequestMapping(value="/users/{user_id}/bankaccounts", method=RequestMethod.GET, produces="application/json")
    public ResponseEntity<List<BankAccount>> listAllBankAcc(@PathVariable String user_id) {
    	List<BankAccount> bankAccList = null;
    	ResponseEntity<List<BankAccount>> respEntity = null;
    	
    	if(allUsersInfo != null && allUsersInfo.getBankAccountMap() != null && allUsersInfo.getBankAccountMap().containsKey(user_id)) {
    		bankAccList = allUsersInfo.getBankAccountMap().get(user_id);
    		respEntity = new ResponseEntity<List<BankAccount>>(bankAccList, HttpStatus.OK);
    	} else {
    		bankAccList = new ArrayList<BankAccount>();
    		respEntity = new ResponseEntity<List<BankAccount>>(bankAccList, HttpStatus.NO_CONTENT);
    	}
    	
    	return respEntity;
    }
    
	/**
	 * Delete Bank Account
	 * @param user_id
	 * @param ba_id
	 * @return
	 */
	@RequestMapping(value="/users/{user_id}/bankaccounts/{ba_id}", method=RequestMethod.DELETE)
    public ResponseEntity<List<BankAccount>> deleteBankAcc(@PathVariable String user_id, @PathVariable String ba_id) {
    	List<BankAccount> bankAccList = new ArrayList<BankAccount>();
    	
    	if(allUsersInfo != null && allUsersInfo.getBankAccountMap() != null && allUsersInfo.getBankAccountMap().containsKey(user_id)) {
    		bankAccList = allUsersInfo.getBankAccountMap().get(user_id);
			for(BankAccount bankAcc : bankAccList) {
	    		if(ba_id != null && ba_id.equalsIgnoreCase(bankAcc.getBa_id())) {
	    			bankAccList.remove(bankAcc);
	    			break;
	    		}
	    	}
    	}
    	
		return new ResponseEntity<List<BankAccount>>(bankAccList, HttpStatus.NO_CONTENT);  
    }
	
	/**
     * Method is to check if the String is empty or NULL
     * @param str
     * @return String
     */
    public String chkIsNull(String str) {
    	String result = null;
    	if(null == str || emptyString.equalsIgnoreCase(str)) {
    		result = emptyString;
    	} else {
    		result = str;
    	}
		return result;
    }

}
