package logiclayer.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import datalayer.DataBaseAccess;
import logiclayer.model.PaymentMethod;

public class PaymentMethodManager implements DataBaseAccess
{

    private static ArrayList<PaymentMethod> paymentMethods;
    private static PaymentMethod defaultPaymentMethod;

    //allows only one object at once
    private static volatile PaymentMethodManager instance;

    private PaymentMethodManager()
    {
        loadPaymentMethods();
        if (paymentMethods.size() == 0)
        {
            revertToPresetPaymentMethods();
        }
    }

    static PaymentMethodManager getInstance()
    {
        if (instance == null)
        {
            synchronized (PaymentMethodManager.class)
            {
                if (instance == null)
                {
                    instance = new PaymentMethodManager();
                }
            }
        }
        return instance;
    }

    public ArrayList<PaymentMethod> getPaymentMethods()
    {
        return paymentMethods;
    }


    public PaymentMethod getDefaultPaymentMethod()
    {
        return defaultPaymentMethod;
    }

    public String[] getPaymentMethodNames()
    {
        String[] paymentMethodNames = new String[paymentMethods.size()];
        for (int i = 0; i < paymentMethods.size(); i++)
        {
            paymentMethodNames[i] = paymentMethods.get(i).getName();
        }
        return paymentMethodNames;
    }

    public int[] getPaymentMethodIcons()
    {
        int[] paymentMethodIcons = new int[paymentMethods.size()];
        for (int i = 0; i < paymentMethods.size(); i++)
        {
            paymentMethodIcons[i] = paymentMethods.get(i).getIconAdress();
        }
        return paymentMethodIcons;
    }

    //returns operation success
    public boolean addPaymentMethod(PaymentMethod paymentMethod)
    {
        boolean successful = paymentMethods.add(paymentMethod);
        if (successful)
        {
            //alphabetic sorting of payment methods
            Comparator comparator = new Comparator<PaymentMethod>()
            {
                public int compare(PaymentMethod method1, PaymentMethod method2)
                {
                    int compare = method1.getNotDeletable().compareTo(method2.getNotDeletable());

                    if (compare == 0)
                    {
                        compare = method1.getName().compareTo(method2.getName());
                    }
                    return compare;
                }
            };

            Collections.sort(paymentMethods, comparator);

            saveAndOverwritePaymentMethods();

        }
        return successful;
    }

    //returns operation success
    public boolean removePaymentMethod(int position)
    {
        if (position >= paymentMethods.size())
        {
            return false;
        }

        databaseHelper.removeObject(paymentMethods.get(position), databaseHelper.TABLE_PAYMENT_METHODS);
        paymentMethods.remove(position);
        return true;
    }

    //returns operation success
    public boolean removePaymentMethod(PaymentMethod paymentMethod)
    {
        for (int i = 0; i < paymentMethods.size(); i++)
        {
            if (paymentMethods.get(i).getName().equals(paymentMethod.getName()))
            {
                databaseHelper.removeObject(paymentMethod, databaseHelper.TABLE_PAYMENT_METHODS);
                paymentMethods.remove(i);
                return true;
            }
        }
        return false;
    }

    //returns operation success
    public boolean setDefaultPaymentMethod(int position)
    {
        if (position >= paymentMethods.size())
        {
            return false;
        }

        defaultPaymentMethod = paymentMethods.get(position);
        databaseHelper.clearAndAddObject(defaultPaymentMethod, databaseHelper.TABLE_DEFAULT_PAYMENT_METHOD);

        return true;
    }

    //returns operation success
    private boolean saveAndOverwritePaymentMethods()
    {
        boolean successful = true;
        successful = successful && databaseHelper.addObjectArrayList(paymentMethods, databaseHelper.TABLE_PAYMENT_METHODS);
        successful = successful && databaseHelper.clearAndAddObject(defaultPaymentMethod, databaseHelper.TABLE_DEFAULT_PAYMENT_METHOD);
        return successful;
    }


    private void loadPaymentMethods()
    {
        paymentMethods = (ArrayList<PaymentMethod>) databaseHelper.getObjects(databaseHelper.TABLE_PAYMENT_METHODS, PaymentMethod.class);
        defaultPaymentMethod = (PaymentMethod) databaseHelper.getFirstObject(databaseHelper.TABLE_DEFAULT_PAYMENT_METHOD, PaymentMethod.class);
    }


    public void revertToPresetPaymentMethods()
    {
        paymentMethods.clear();
        addPaymentMethod(new PaymentMethod("Cash", "icon_cash"));
        defaultPaymentMethod = paymentMethods.get(paymentMethods.size() - 1);
        addPaymentMethod(new PaymentMethod("Cheque", "icon_cheque"));
        addPaymentMethod(new PaymentMethod("Credit card", "icon_creditcard"));
        addPaymentMethod(new PaymentMethod("Debit card", "icon_debitcard"));
        addPaymentMethod(new PaymentMethod("Direct debit", "icon_bank"));
        addPaymentMethod(new PaymentMethod("PayPal", "icon_paypal"));
        addPaymentMethod(new PaymentMethod("Other", "icon_points", true));
    }

    public int getPaymentMethodPosition(PaymentMethod paymentMethod)
    {
        for (int i = 0; i < paymentMethods.size(); i++)
        {
            if (paymentMethods.get(i).getName().equals(paymentMethod.getName()))
            {
                return i;
            }
        }
        //not successful
        return -1;
    }
}
