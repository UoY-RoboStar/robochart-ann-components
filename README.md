# RoboChart ANN Components

This project contains several software artefacts related to the in-development RoboChart components that are implemented by artificial neural networks (ANN components).

The `Metamodel+WFConditions` directory contains a modelling project, `ANNComponents`, that presents the RoboChart metamodel with ANN (artificial neural network) components integrated, and the well-formedness conditions of the ANN components formalised in Z.

The `Segway_Case_Study` directory contains files relevant to the Segway case study. It contains: 
 - A `RoboChart` directory, that contains a RoboChart project `SegwayComparison` containinng all four versions of the Segway RoboChart model, 
   for the purpose of comparing their CSP semantics. 
 - A directory `Segway_Proofs` that contains CSP files for proving properties about the automatically generated CSP semantics from the 
   `SegwayComparison` project, and those semantics with an ANN process added manually. 

The `Supporting Proofs` directory contains CSPM documents establishing proofs required for our verification strategy. 