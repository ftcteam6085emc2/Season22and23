package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.hardware.CRServo;

@TeleOp(name="Test", group="Linear Opmode")

public class Test extends LinearOpMode {

    private DcMotor frontLeft = null;
    private DcMotor backLeft = null;
    private DcMotor frontRight = null;
    private DcMotor backRight = null;
    
    CRServo clawServo = null;
    
    private double maxDrivePower = 1;

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backRight = hardwareMap.get(DcMotor.class, "backRight");
        
        clawServo = hardwareMap.get(CRServo.class, "clawServo");
        
        frontRight.setDirection(DcMotor.Direction.REVERSE);
        backRight.setDirection(DcMotor.Direction.REVERSE);
        
        waitForStart();
        
        

        while (opModeIsActive()) {
            
            double rightDrive = gamepad1.right_stick_y * maxDrivePower;
            double leftDrive =  gamepad1.left_stick_y * maxDrivePower;
            double rightStrafe = gamepad1.right_stick_x * maxDrivePower;
            double leftStrafe = gamepad1.left_stick_x * maxDrivePower;

            frontLeft.setPower((-leftDrive + leftStrafe)/maxDrivePower);
            backLeft.setPower((-leftDrive - leftStrafe)/maxDrivePower);
            frontRight.setPower((-rightDrive - rightStrafe)/maxDrivePower);
            backRight.setPower((-rightDrive + rightStrafe)/maxDrivePower);
            
            if(gamepad2.right_trigger > 0.25){
                clawServo.setPower(1);
            }else if(gamepad2.left_trigger > 0.25){
                clawServo.setPower(-1);
            }else{
                clawServo.setPower(0);
            }
            
            if(gamepad1.b){
                maxDrivePower = 0.5;
                telemetry.addData("Status", "Half power selected");
                telemetry.addData("maxDrivePower", maxDrivePower);
                telemetry.update();
            }
            if(gamepad1.a){
                maxDrivePower = 1.0;
                telemetry.addData("Status", "Full power selected", maxDrivePower);
                telemetry.addData("maxDrivePower", maxDrivePower);
                telemetry.update();
            }
        }
    }
}