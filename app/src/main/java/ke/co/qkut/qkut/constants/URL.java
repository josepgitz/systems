package ke.co.qkut.qkut.constants;

public  final class  URL {
    public  static  String BASE_URL="https://qkut.co.ke/";
    public  static  String GET_PLACES_FOR_GUEST=BASE_URL+"api/listBusinesses?q=";
    public  static  String GET_PLACES_FOR_NON_GUEST=BASE_URL+"api/auth/listBusinesses?q=";
    public  static  String USER_CONFIRM_VISIT=BASE_URL+"api/auth/user/visit";
    public static  String USER_LOGIN=BASE_URL+"api/auth/login";
    public  static  String USER_ACCOUNT=BASE_URL+"api/auth/user";
    public  static  String USER_PROFILE_PIC=BASE_URL+"api/auth/user/getProfilePhoto";
    public  static  String USER_QR=BASE_URL+"api/auth/user/getQR";
    public  static  String USER_TOKEN=BASE_URL+"api/auth/storeTokens";
    public  static  String ADD_USER_TO_QUEUE=BASE_URL+"api/auth/user/queue";
    public static  String GET_SCHEDULES=BASE_URL+"api/auth/user/getSchedules";
    public static  String REGISTER_USER=BASE_URL+"api/register";
    public static  String GET_VERIFACTION=BASE_URL+"api/verification";

}
