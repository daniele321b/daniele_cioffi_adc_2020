package it.unisa.adc.p2p.interfaces;

import java.util.List;

public interface SocialInterface {

	/**
	 * Gets the social network users questions.
	 * 
	 * @return a list of String that is the profile questions.
	 */
	public List<String> getUserProfileQuestions();

	/**
	 * Creates a new user profile key according the user answers.
	 * 
	 * @param _answer a list of answers.
	 * @return a String, the obtained profile key.
	 */
	public String createAuserProfileKey(List<Integer> _answer);

	/**
	 * Joins in the Network. An automatic messages to each potential new friend is
	 * generated.
	 * 
	 * @param _profile_key a String, the user profile key according the user answers
	 * @param _nick_name   a String, the nickname of the user in the network.
	 * @return true if the join success, fail otherwise.
	 */
	public boolean join(String _profile_key, String _nick_name);

	/**
	 * Gets the nicknames of all automatically creates friendships.
	 * 
	 * @return a list of String.
	 */
	public List<String> getFriends();

}