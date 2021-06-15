package logiclayer.controller;

public interface ManagerAccess
{
    public CategoryManager categoryManager = CategoryManager.getInstance();
    public EntryManager entryManager = EntryManager.getInstance();
    public PaymentMethodManager paymentMethodManager = PaymentMethodManager.getInstance();
    public RecurringEntryManager recurringEntryManager = RecurringEntryManager.getInstance();
    public SettingsManager settingsManager = SettingsManager.getInstance();
    public IntervalManager intervalManager = IntervalManager.getInstance();



}
