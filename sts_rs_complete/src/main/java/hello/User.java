package hello;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;


/**
 * @author Varun
 * User Bean
 */
public class User {
	
	private String user_id;
	@NotEmpty @NotNull @Email
    private String email;
	@NotEmpty @NotNull @Size(min=2)
    private String password;
	@NotEmpty @NotNull @Size(min=2, max=30)
    private String name;
    private String created_at;
    private String updated_at;
    
    /**
     * Default Constructor
     */
    public User(){
    	
    }
    
    /**
     * Parameterized Constructor
     * @param user_id
     * @param email
     * @param password
     * @param name
     * @param created_at
     * @param updated_at
     */
    public User(String user_id, String email, String password, String name, String created_at, String updated_at) {
        this.user_id = user_id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

	/**
	 * @return the user_id
	 */
	public String getUser_id() {
		return user_id;
	}

	/**
	 * @param user_id the user_id to set
	 */
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the created_at
	 */
	public String getCreated_at() {
		return created_at;
	}

	/**
	 * @param created_at the created_at to set
	 */
	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	/**
	 * @return the updated_at
	 */
	public String getUpdated_at() {
		return updated_at;
	}

	/**
	 * @param updated_at the updated_at to set
	 */
	public void setUpdated_at(String updated_at) {
		this.updated_at = updated_at;
	}

}
