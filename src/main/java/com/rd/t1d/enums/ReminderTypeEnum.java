package com.rd.t1d.enums;

public enum ReminderTypeEnum {

    BOLUS_INSULIN_BUY("bolus_insulin_buy", "Please buy the bolus insulin pack"),
    BASAL_INSULIN_BUY("basal_insulin_buy", "Please buy the basal insulin pack"),
    SYRINGE_BUY("syringe_buy", "Please buy a new pack of syringes"),
    SYRINGE_CHANGE("syringe_change", "Please replace your syringe with a new one"),
    BOLUS_INSULIN_PEN_FILL("bolus_insulin_pen_fill", "Please fill you bolus insulin pen"),
    BASAL_INSULIN_PEN_FILL("basal_insulin_pen_fill", "Please fill you basal insulin pen"),
    INSULIN_PEN_BUY("insulin_pen_buy", "Please buy a new insulin pen"),
    INSULIN_PEN_CHANGE("insulin_pen_change", "Please replace your insulin pen with a new one"),
    INSULIN_PEN_NEEDLE_BUY("insulin_pen_needle_buy", "Please buy a new pack of insulin pen needle"),
    INSULIN_PEN_NEEDLE_CHANGE("insulin_pen_needle_change", "Please replace your insulin pen needle with a new one"),
    PUMP_INFUSION_SET_BUY("pump_infusion_set_buy", "Please buy a new pack of pump infusion set"),
    PUMP_INFUSION_SET_CHANGE("pump_infusion_set_change", "Please replace your pump infusion set"),
    PUMP_RESERVOIR_BUY("pump_infusion_set_buy", "Please buy a new pack of pump reservoir set"),
    PUMP_RESERVOIR_CHANGE("pump_infusion_set_change", "Please replace your pump reservoir set"),
    PUMP_BATTERY_BUY("pump_battery_set", "Please buy a new pack of pump battery"),
    PUMP_BATTERY_CHANGE("pump_battery_set", "Please replace your pump battery"),
    GLUCOMETER_BATTERY_BUY("glucometer_battery_buy", "Please buy a new pack of glucometer battery"),
    GLUCOMETER_BATTERY_CHANGE("glucometer_battery_change", "Please replace your glucometer battery"),
    GLUCOMETER_STRIPS_BUY("glucometer_strips_buy", "Please buy a new pack of glucometer strips pack"),
    LANCET_BUY("lancet_buy", "Please buy a new pack of lancet"),
    LANCET_CHANGE("lancet_change", "Please replace your lancet"),
    CGM_BUY("cgm_buy", "Please buy a new pack of CGM"),
    CGM_CHANGE("cgm_change", "Please replace your CGM"),
    SWEET_BUY("sweet_buy", "Please buy your favourite sweet to treat your hypo");

    private String code;

    private String message;

    private ReminderTypeEnum(String code, String message){
        this.code = code;
        this.message = message;
    }

    /*public static ReminderType getReminderType(String name){
        for(ReminderType reminderType : ReminderType.values()){
            if(reminderType.name.equalsIgnoreCase(name)){
                return reminderType;
            }
        }

        throw new T1DBuddyException(ErrorCode.WRONG_RELATION, HttpStatus.BAD_REQUEST);
    }*/

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
