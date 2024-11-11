package com.Soganis.Service;

import com.Soganis.Entity.*;
import com.Soganis.Repository.*;
import org.springframework.stereotype.Service;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private ItemsRepository itemRepo;

    @Autowired
    private UserSalaryRepository userSalaryRepo;

    @Autowired
    private BillingRepository billRepo;

    @Autowired
    private UserCashCollectionRepository userCashCollectionRepo;

    @Autowired
    private UserMonthlySalaryRepository userMonthlySalaryRepository;

    @Autowired
    private CustomerOrderRepo customerOrderRepo;

    @Autowired
    private  ItemService itemService;

    @Autowired
    private StoreRepository storeRepo;

    @Autowired
    private  SchoolRepository schoolRepo;



    public User getUserInfo(String userId) {
        Optional<User> opt = userRepo.findById(userId);
        if (opt.isPresent()) {
            User user = opt.get();
            return user;
        }
        return null;
    }

    public List<User> getUserList(String storeId) {
        List<User> lst = userRepo.getUserByStoreId(storeId);
        return lst;

    }

    public String userSalaryUpdate(List<User_Salary> salary,String storeId) {
        try {
            List<User_Salary> salaryList=new ArrayList<>();
            for(User_Salary userSalary:salary)
            {
                userSalary.setStoreId(storeId);
                salaryList.add(userSalary);
            }
            userSalaryRepo.saveAll(salaryList);
            return "Success";
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed";
        }
    }

    public String updateCustomerOrder(CustomerOrderBook order,String storeId) {
        try {
            // Ensure that each order in the list has a reference to the CustomerOrderBook
            for (Order orderItem : order.getOrders()) {
                orderItem.setCustomerOrderBook(order);
            }
            int totalAmount = order.getTotalAmount();
            int amount_due = totalAmount - order.getAdvancePayment();
            order.setAmount_due(amount_due);
            order.setStatus("PENDING");
            order.setStoreId(storeId);
            customerOrderRepo.save(order);
            return "Success";
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed";
        }
    }

    public List<CustomerOrderBook> customerOrderDetails(String status,String storeId) {
        List<CustomerOrderBook> lst = customerOrderRepo.findByStatus(status,storeId);
        return lst;
    }

    public String validateStoreId(String storeId)
    {
        Optional<Store> opt=storeRepo.findById(storeId);
        if(opt.isPresent())
        {
            return "Exists";
        }
        else
        {
            return "New";
        }
    }

    public String createStore(Store store)
    {
        try {
            storeRepo.save(store);
            return "Success";

        }catch (Exception e)
        {
            e.printStackTrace();
            return "Failed";
        }
    }

    public List<Store> getStore()
    {
        List<Store> stores=storeRepo.findAll();
        return stores;
    }

    public List<User> getAllUser()
    {
        List<User> users=userRepo.findAll();
        return users;
    }

    public User validateUser(User user)
    {
        Optional<User> opt=userRepo.findById(user.getUserId());
        if(opt.isPresent())
        {
            User validatUser= opt.get();
            if(validatUser.getRole().equals("Owner"))
            {
               return validatUser;
            }
            else {
                return null;
            }
        }
        else{
            return null;
        }

    }

    public String validateUserId(String userId)
    {
        Optional<User> opt=userRepo.findById(userId);
        if(opt.isPresent())
        {
            return "Exists";
        }
        else{
            return "New";
        }
    }

    public String createUser(User user)
    {
        try {
            userRepo.save(user);
            return "Success";

        }catch (Exception e)
        {
            e.printStackTrace();
            return "Failed";
        }
    }


    public String updateOrderDetailDelivered(int orderId) {
        Optional<CustomerOrderBook> opt = customerOrderRepo.findById(orderId);
        if (opt.isPresent()) {
            CustomerOrderBook order = opt.get();
            order.setStatus("DELIVERED");
            customerOrderRepo.save(order);
            return "success";
        } else {
            return "failed";
        }
    }

    public String updateOrderDetailCancelled(int orderId) {
        Optional<CustomerOrderBook> opt = customerOrderRepo.findById(orderId);
        if (opt.isPresent()) {
            CustomerOrderBook order = opt.get();
            order.setStatus("CANCELLED");
            customerOrderRepo.save(order);
            return "success";
        } else {
            return "failed";
        }
    }

    public int salaryDeduction(String userId, String type, int hours) {

        Optional<User> opt = userRepo.findById(userId);
        User user = new User();
        if (opt.isPresent()) {
            user = opt.get();
        }

        int amount = 0;
        int yearly_salary = user.getMonthly_salary() * 12;
        int per_day_salary = yearly_salary / 365;

        if (type.equals("ABSENT")) {
            amount = per_day_salary;
        }

        if (type.equals("HALF_DAY")) {
            amount = per_day_salary / 2;
        }

        if (type.equals("HOURLY_DEDUCTION")) {
            amount = (per_day_salary / 10) * hours;
        }

        return amount;
    }

    public List<UserCashCollection> userCashCollectionReportByDate(String storeId,Date startDate,Date endDate) {
        List<User> users = userRepo.getUserByStoreId(storeId);
        List<UserCashCollection> userCashList = new ArrayList<>();
        List<Date> dates=getAllDatesBetweenDates(startDate,endDate);



        for(Date date:dates) {
            for (User user : users) {
                UserCashCollection user_cash_collection = new UserCashCollection();
                List<Billing> bills = billRepo.findByUserIdAndBillDate(user.getUserId(), date, storeId);

                int user_cash_collected = bills.stream()
                        .mapToInt(Billing::getFinal_amount)
                        .filter(amount -> amount > 0)
                        .sum();

                int user_cash_returned = bills.stream()
                        .mapToInt(Billing::getFinal_amount)
                        .filter(amount -> amount < 0)
                        .sum();

                user_cash_returned = user_cash_returned * -1;
                int total = user_cash_collected - user_cash_returned;
                user_cash_collection.setUserId(user.getUserId());
                user_cash_collection.setCollection_date(date);
                user_cash_collection.setUserName(user.getSname());
                user_cash_collection.setCash_collection(user_cash_collected);
                user_cash_collection.setCash_return(user_cash_returned);
                user_cash_collection.setFinal_cash_collection(total);
                user_cash_collection.setStoreId(storeId);
                if(user_cash_collected==0&& user_cash_returned==0 && total==0 )
                {
                    continue;
                }
                userCashList.add(user_cash_collection);

            }
            userCashCollectionRepo.saveAll(userCashList);
        }

       List<UserCashCollection> userCashCollections=userCashCollectionRepo.findByDateRangeAndStoreId(startDate,endDate,storeId);
        List<UserCashCollection> userCashCollectionFinalList = userCashCollections.stream()
                .filter(entry -> entry.getFinal_cash_collection() != 0)
                .collect(Collectors.toList());

        return userCashCollectionFinalList;

    }

    public static List<Date> getAllDatesBetweenDates(Date startDate, Date endDate) {
        List<Date> dates = new ArrayList<>();

        // Set up a calendar instance starting from startDate
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);

        // Iterate until we reach the end date
        while (!calendar.getTime().after(endDate)) {
            dates.add(calendar.getTime()); // Add current date to the list
            calendar.add(Calendar.DATE, 1); // Move to the next day
        }

        return dates;
    }

    public String updateUserCashCollectionReport() {
        try {
            List<User> users = userRepo.findAll();

            List<UserCashCollection> userCashList = new ArrayList<>();
            for (User user : users) {
                String storeId=itemService.getStoreId(user.getUserId());
                UserCashCollection user_cash_collection = new UserCashCollection();
                List<Billing> bills = billRepo.findByUserIdAndBillDate(user.getUserId(), new Date(),storeId);

                int user_cash_collected = bills.stream()
                        .mapToInt(Billing::getFinal_amount)
                        .filter(amount -> amount > 0)
                        .sum();

                int user_cash_returned = bills.stream()
                        .mapToInt(Billing::getFinal_amount)
                        .filter(amount -> amount < 0)
                        .sum();

                user_cash_returned = user_cash_returned * -1;
                int total = user_cash_collected - user_cash_returned;
                user_cash_collection.setUserId(user.getUserId());
                user_cash_collection.setCollection_date(new Date());
                user_cash_collection.setUserName(user.getSname());
                user_cash_collection.setCash_collection(user_cash_collected);
                user_cash_collection.setCash_return(user_cash_returned);
                user_cash_collection.setStoreId(storeId);
                user_cash_collection.setFinal_cash_collection(total);

                if(user_cash_collected==0&& user_cash_returned==0 && total==0 )
                {
                    continue;
                }
                userCashList.add(user_cash_collection);

            }
            userCashCollectionRepo.saveAll(userCashList);
        }catch (Exception e)
        {
            System.out.println("Exception at User Cash");
        }
        return "Success";

    }

    public List<UserMonthlySalary> generateUserMonthlySalaries(String month_fy,String storeId) {
        List<UserMonthlySalary> salaries = new ArrayList<>();
        List<User_Salary> salary_statement;
        List<User> users = userRepo.getUserByStoreId(storeId);

        String[] parts = month_fy.split("_");
        int month = Integer.parseInt(parts[0]);
        int year = Integer.parseInt(parts[1]);
        Month monthEnum = Month.of(month);
        String monthName = monthEnum + "";
        month_fy = monthName + " " + year;

        for (User user : users) {
            UserMonthlySalary salary = new UserMonthlySalary();
            salary_statement = userSalaryRepo.findUserSalaryByYearMonth(user.getUserId(), year, month);
            int advanceSalary = sumAmountsByType(salary_statement, "ADVANCE");
            int absentSalaryDeduction = sumAmountsByType(salary_statement, "ABSENT");
            int salaryHalfDay = sumAmountsByType(salary_statement, "HALF_DAY");
            int salaryHourlyDeduction = sumAmountsByType(salary_statement, "HOURLY_DEDUCTION");
            int totalSalaryDeducted = absentSalaryDeduction + salaryHalfDay + salaryHourlyDeduction;
            int totalAmount = user.getMonthly_salary() - totalSalaryDeducted - advanceSalary; // calcualting salaries
            salary.setUserId(user.getUserId());
            salary.setUser_name(user.getSname());
            salary.setMonth_fy(month_fy);
            salary.setSalaryDeducted(totalSalaryDeducted);
            salary.setAdvanceSalary(advanceSalary);
            salary.setFinalAmount(totalAmount);
            salary.setMonthlySalary(user.getMonthly_salary());
            salary.setStoreId(storeId);

            UserMonthlySalaryId id = new UserMonthlySalaryId(user.getUserId(), month_fy);
            System.out.println("Id: " + id);
            Optional<UserMonthlySalary> opt = userMonthlySalaryRepository.findById(id);
            if (opt.isPresent()) {
                UserMonthlySalary userMonthlySalary = opt.get();
                salary.setStatus(userMonthlySalary.getStatus());
            } else {
                salary.setStatus("PENDING");
            }

            salaries.add(salary);
        }
        userMonthlySalaryRepository.saveAll(salaries);

        return salaries;
    }

    public List<User_Salary> getUserSalaryStatement(String userId, String month_fy) {

        List<User_Salary> salary_statement;

        String[] parts = month_fy.split(" ");
        String monthString = parts[0];
        int year = Integer.parseInt(parts[1]);
        int month = convertMonthToInt(monthString);

        try {
            salary_statement = userSalaryRepo.findUserSalaryByYearMonth(userId, year, month);
            return salary_statement;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public String userMonthlySalaryChangeStatus(UserMonthlySalary salary) {
        try {
            System.out.println(salary);
            salary.setStatus("PAID");
            String storeId=itemService.getStoreId(salary.getUserId());
            salary.setStoreId(storeId);
            userMonthlySalaryRepository.save(salary);
            return "Salary updated";

        } catch (Exception e) {
            e.printStackTrace();
            return "Unable to update status";
        }
    }

    public int sumAmountsByType(List<User_Salary> salaries, String type) {
        return salaries.stream()
                .filter(salary -> type.equals(salary.getType()))
                .mapToInt(User_Salary::getAmount)
                .sum();
    }

    public int convertMonthToInt(String month) {
        switch (month.toUpperCase()) {
            case "JANUARY":
                return 1;
            case "FEBRUARY":
                return 2;
            case "MARCH":
                return 3;
            case "APRIL":
                return 4;
            case "MAY":
                return 5;
            case "JUNE":
                return 6;
            case "JULY":
                return 7;
            case "AUGUST":
                return 8;
            case "SEPTEMBER":
                return 9;
            case "OCTOBER":
                return 10;
            case "NOVEMBER":
                return 11;
            case "DECEMBER":
                return 12;
            default:
                throw new IllegalArgumentException("Invalid month: " + month);
        }
    }

    public List<String> getSchoolList(String storeId) {
        List<String> lst = itemRepo.findDistinctItemCategories(storeId);
        return lst;
    }

    public List<SchoolList> getSchoolNameCode(String storeId) {
        List<SchoolList> lst = schoolRepo.findSchoolNameCode(storeId);
        return lst;
    }




    public List<String> getFilteredSchoolList(String searchTerm, String storeId) {
        List<String> lst = schoolRepo.findAllSchool(searchTerm,storeId);
        return lst;
    }


    public List<String> itemTypeList(String storeId) {
        List<String> lst = itemRepo.findDistinctItemTypes(storeId);
        return lst;
    }

    public List<String> itemTypeList(String schoolName,String storeId) {
        List<String> lst = itemRepo.findDistinctItemTypesBySchool(schoolName,storeId);
        return lst;
    }

    public List<Items> itemListBySchool(String schoolCode,String storeId) {
        List<Items> lst = itemRepo.findItemsBySchool(schoolCode,storeId);
        return lst;

    }

    public List<Items> getAllItemstore(String storeId) {
        List<Items> lst = itemRepo.getAllItemsStores(storeId);
        return lst;

    }



    public List<Items> itemListByItemType(String itemType,String storeId) {
        List<Items> lst = itemRepo.findItemsByItemType(itemType,storeId);
        return lst;
    }

    public List<Items> itemListBySchoolAndType(String itemCategory, String itemType,String storeId) {
        List<Items> lst = itemRepo.findItemsBySchoolAndType(itemCategory, itemType,storeId);
        return lst;
    }

    public List<Billing> getBillByMobileNo(String mobileNo,String storeId)
    {
      List<Billing> billings=billRepo.getBillByMobileNo(mobileNo,storeId);
      return  billings;
    }

    public String getStore(String storeId)
    {
        Optional<Store> opt=storeRepo.findById(storeId);
        if(opt.isPresent())
        {
            Store store=opt.get();
            String storeName=store.getStoreName().toUpperCase();
            return storeName;
        }
        else {
            return "Sogani NX";
        }
    }

}
