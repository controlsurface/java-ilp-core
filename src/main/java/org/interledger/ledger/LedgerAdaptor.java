package org.interledger.ledger;

import org.interledger.InterledgerAddress;
import org.interledger.InterledgerException;
import org.interledger.cryptoconditions.Fulfillment;
import org.interledger.ledger.events.LedgerConnectEvent;
import org.interledger.ledger.events.LedgerEvent;
import org.interledger.ledger.events.LedgerEventHandler;
import org.interledger.ledger.model.AccountInfo;
import org.interledger.ledger.model.LedgerInfo;
import org.interledger.ledger.model.LedgerMessage;
import org.interledger.ledger.model.LedgerTransfer;

import java.util.Set;
import java.util.UUID;

public interface LedgerAdaptor {

  /**
   * Async (non-blocking) connect request. The adaptor should raise an {@link LedgerConnectEvent}
   * when it has connected to the ledger.
   */
  void connect();

  /**
   * Tests if the adaptor is connected to the ledger or not.
   */
  boolean isConnected();

  /**
   * Disconnects the adaptor from the ledger if it is connected.
   */
  void disconnect();

  /**
   * Sends a message to the ledger.
   *
   * @param message The message to send.
   */
  void sendMessage(LedgerMessage message);

  /**
   * Registers an event handler that will be notified on ledger events.
   *
   * @param eventHandler The event handler to register.
   */
  void setEventHandler(LedgerEventHandler eventHandler);

  /**
   * Retrieves information about the ledger the adaptor is connected to. Note that this information
   * may be cached.
   */
  LedgerInfo getLedgerInfo();

  /**
   * Initiates a ledger-local transfer.
   *
   * @param transfer {@link LedgerTransfer}
   */
  void sendTransfer(LedgerTransfer transfer);

  /**
   * TODO:(0) Clarify. In five-bells-ledger "rejection" is used for proposed (not yet prepared
   *     transfers) and debitors involved in transfer can reject at any point while the
   *     transfer is not in prepared state (Note: Proposed state is not part of the RFCs anymore)
   *
   * Reject a transfer. This should only be allowed if the entity rejecting the transfer is the
   * receiver.
   * Note: Rejecting a transfer is different to cancelling it:
   *  - A transfer is rejected  when some error is found (like wrong parameters or timeout)
   *  - A transfer is cancelled when the cancellation fulfillment matching the cancellation condition is sent
   *
   * @param transfer The transfer being proposed.
   * @param reason   The reason (InterledgerException) why the transfer should be rejected.
   */
  void rejectTransfer(LedgerTransfer transfer, InterledgerException reason);

  /**
   * Get basic details of an account.
   *
   * @param account The local account identifier
   * @return Details of the account requested.
   */
  AccountInfo getAccountInfo(InterledgerAddress account);

  /**
   * Subscribe to notifications related to an account. Notifications that are received for this
   * account should then be raised as a {@link LedgerEvent} and passed to the
   * {@link LedgerEventHandler} for the adaptor.
   *
   * @param account The account of interest.
   */
  void subscribeToAccountNotifications(InterledgerAddress account);

  /**
   * Returns the set of ledger accounts that are known to be owned by connectors.
   */
  Set<InterledgerAddress> getConnectors();

  /**
   * Fulfill a condition for a transfer that has been prepared on a ledger.
   *
   * The fulfillment can be (TODO: Recheck next claims):
   *   - execution    condition: executes  the transfer if fulfillment match the stored execution    condition
   *   - cancellation condition: rollbacks the transfer if fulfillment match the stored cancellation condition
   *
   * @param transferId  The unique transfer identifier.
   * @param fulfillment The fulfillment for the transfer.
   */
  void fulfillTransfer(UUID transferId, Fulfillment fulfillment);
}
