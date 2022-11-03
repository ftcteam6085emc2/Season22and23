package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name="Test", group="Linear Opmode")

public class Test extends LinearOpMode {

    private DcMotor frontLeft = null;
    private DcMotor backLeft = null;
    private DcMotor frontRight = null;
    private DcMotor backRight = null;
    
    private double maxDrivePower = 1;

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backRight = hardwareMap.get(DcMotor.class, "backRight");
        
        frontRight.setDirection(DcMotor.Direction.REVERSE);
        backRight.setDirection(DcMotor.Direction.REVERSE);
        
        waitForStart();

        while (opModeIsActive()) {
            double rightDrive = gamepad1.right_stick_y * maxDrivePower;
            double leftDrive =  gamepad1.left_stick_y * maxDrivePower;
            double rightStrafe = gamepad1.right_stick_x * maxDrivePower;
            double leftStrafe = gamepad1.left_stick_x * maxDrivePower;
            
            frontLeft.setPower((leftDrive + leftStrafe)/maxDrivePower);
            backLeft.setPower((leftDrive - leftStrafe)/maxDrivePower);
            frontRight.setPower((rightDrive - rightStrafe)/maxDrivePower);
            backRight.setPower((rightDrive + rightStrafe)/maxDrivePower);
        }
    }
}
