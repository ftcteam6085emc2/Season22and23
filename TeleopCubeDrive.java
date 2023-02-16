package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.hardware.CRServo;

@TeleOp(name="TeleopCubeDrive", group="Linear Opmode")

public class TeleopCubeDrive extends LinearOpMode {

    private DcMotor frontLeft = null;
    private DcMotor backLeft = null;
    private DcMotor frontRight = null;
    private DcMotor backRight = null;
    
    private DcMotor linearSlide = null;
    
    private Servo clawServo = null;
    
    private double maxDrivePower = 0.6;
    private double slideSpeed = 1;
    
    private double offset = 0.675;
    private double clawClose = 0;
    private double clawOpen = 0.75;

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backRight = hardwareMap.get(DcMotor.class, "backRight");
        
        linearSlide = hardwareMap.get(DcMotor.class, "linearSlide");
        
        clawServo = hardwareMap.get(Servo.class, "clawServo");
        
        frontRight.setDirection(DcMotor.Direction.REVERSE);
        backRight.setDirection(DcMotor.Direction.REVERSE);
        
        waitForStart();

        while (opModeIsActive()) {
            double drive = gamepad1.left_stick_y;
            double strafe = -gamepad1.left_stick_x;
            double turn = -gamepad1.right_stick_x;
            
            frontLeft.setPower((drive - strafe + turn) * maxDrivePower);
            backLeft.setPower((drive + strafe + turn) * maxDrivePower);
            frontRight.setPower((drive + strafe - turn) * maxDrivePower);
            backRight.setPower((drive - strafe - turn) * maxDrivePower);
            
            if(gamepad2.right_trigger > 0.25){
                clawServo.setPosition(clawClose + offset);
            }else{
                clawServo.setPosition(clawOpen + offset);
            }
            
            if(gamepad2.dpad_up){
                linearSlide.setPower(-slideSpeed);
            }else if(gamepad2.dpad_down){
                linearSlide.setPower(slideSpeed);
            }else{
                linearSlide.setPower(0);
            }
        }
    }
}
