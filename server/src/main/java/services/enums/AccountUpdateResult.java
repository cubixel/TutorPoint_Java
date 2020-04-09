package services.enums;

public enum AccountUpdateResult {
  ACCOUNT_UPDATE_SUCCESS,
  FAILED_BY_CREDENTIALS,
  FAILED_BY_USERNAME_TAKEN,
  FAILED_BY_EMAIL_TAKEN,
  FAILED_BY_NETWORK,
  FAILED_BY_UNEXPECTED_ERROR;
}
