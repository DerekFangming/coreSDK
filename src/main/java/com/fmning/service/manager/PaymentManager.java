package com.fmning.service.manager;

import com.fmning.service.domain.Payment;
import com.fmning.service.exceptions.NotFoundException;

public interface PaymentManager {
	
	/**
	 * Create a payment object and save it into database
	 * @param type
	 * @param mappingId
	 * @param amount
	 * @param status
	 * @param message
	 * @param payerId
	 * @param receiverId
	 * @param method
	 * @param nonce
	 * @return
	 */
	public int createPayment(String type, int mappingId, double amount, String status, String message,
			int payerId, int receiverId, String method, String nonce);
	
	/**
	 * Get payment by database id
	 * @param id the database id
	 * @return the payment object
	 * @throws NotFoundException if the payment does not exist
	 */
	public Payment getPaymentById(int id) throws NotFoundException;
	
	/**
	 * Get payment by type and mapping id
	 * @param type
	 * @param mappingId
	 * @return the payment object
	 * @throws NotFoundException if the payment does not exist
	 */
	public Payment getPaymentByType(String type, int mappingId) throws NotFoundException;
	
	/**
	 * Get payment by type, mapping id, payer id and receiver id
	 * @param type
	 * @param mappingId
	 * @param payerId
	 * @param receiverId
	 * @return the payment object
	 * @throws NotFoundException if the payment does not exist
	 */
	public Payment getPaymentByTypeAndPayer(String type, int mappingId, int payerId, int receiverId)
			throws NotFoundException;
}
