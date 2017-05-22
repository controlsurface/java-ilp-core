package org.interledger.ledger.model;

import org.interledger.InterledgerAddress;
import org.interledger.cryptoconditions.Condition;

import java.time.ZonedDateTime;
import java.util.UUID;
import javax.money.MonetaryAmount;


/**
 * Defines a transfer on the ledger.
 */
public interface LedgerTransfer { // TODO:(0) 

  /**
   * Returns the unique id of the transfer.
   */
  UUID getId();

  /**
   * Returns the account from which funds will be transferred.
   */
  InterledgerAddress getFromAccount();

  /**
   * Returns the account to which funds will be transferred.
   */
  InterledgerAddress getToAccount();

  /**
   * Returns the amount being transferred.
   */
  MonetaryAmount getAmount();

  /**
   * TODO:(0) Document
   */
  String getInvoice();

  /**
   * Get the data to be sent.
   *
   * Ledger plugins SHOULD treat this data as opaque, however it will usually
   * start with an ILP header followed by a transport layer header, a quote
   * request or a custom user-provided data packet.
   *
   * If the data is too large, the ledger plugin MUST throw a
   * MaximumDataSizeExceededError. If the data is too large only because the
   * amount is insufficient, the ledger plugin MUST throw an
   * InsufficientAmountError.
   *
   * @return a buffer containing the data
   */
  byte[] getData();

  /**
   * Get the host's internal memo
   *
   * An optional bytestring containing details the host needs to persist with
   * the transfer in order to be able to react to transfer events like
   * condition fulfillment later.
   *
   * Ledger plugins MAY attach the noteToSelf to the transfer and let the
   * ledger store it. Otherwise it MAY use the store in order to persist this
   * field. Regardless of the implementation, the ledger plugin MUST ensure
   * that all instances of the transfer carry the same noteToSelf, even across
   * different machines.
   *
   * Ledger plugins MUST ensure that the data in the noteToSelf either isn't
   * shared with any untrusted party or encrypted before it is shared.
   *
   * @return a buffer containing the data
   */

  byte[] getNoteToSelf();

  /**
   * Returns the condition under which the transfer will be executed.
   */
  Condition getExecutionCondition();

  /**
   * TODO:??.
   */
  Condition getCancellationCondition();

  /**
   * The date when the transfer expires and will be rejected by the ledger.
   */
  ZonedDateTime getExpiresAt();

  /**
   * Indicates if the transfer has been rejected.
   */
  boolean isRejected();

  /**
   * Returns the reason for rejecting the transfer.
   */
  String getRejectionMessage();

}
