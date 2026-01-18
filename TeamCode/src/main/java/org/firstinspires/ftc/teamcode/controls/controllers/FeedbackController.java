package org.firstinspires.ftc.teamcode.controls.controllers;

import org.firstinspires.ftc.teamcode.controls.motion.State;

public interface FeedbackController extends Controller {
    double calculate(State measurement);
}
