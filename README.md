# RoboChart ANN Components

This project contains several software artefacts related to the in-development RoboChart components that are implemented by artificial neural networks (ANN components).

The `Metamodel+WFConditions` directory contains a modelling project, `ANNComponents`, that presents CZT specification files relevant to the ANN components in RoboChart. 
It contains: 
- A directory `Metamodel` that contains the RoboChart metamodel with ANN components integrated. This directory also contains a Z specification of the ANNController class, used to define the denotational semantic rules. 
- A directory `ANN_WF_Conditions_Z` that contains the well-formedness conditions of the ANN components formalised in Z.
- A directory `Circus` that contains in-development versions of the Circus profile of the ANN semantics.

The `Segway_Case_Study` directory contains files relevant to the Segway case study. It contains: 
 - A `RoboChart` directory, that contains a RoboChart project `SegwayComparison` containinng all four versions of the Segway RoboChart model, 
   for the purpose of comparing their CSP semantics. 
 - A `RoboChart_With_ANNs` directory, that contains a RoboChart project `Segway-ANN`, which is the version of the Segway with the new version of RoboTool supporting ANN components.
 - A directory `Segway_Proofs` that contains CSP files for proving properties about the automatically generated CSP semantics from the 
   `SegwayComparison` project, and those semantics with an ANN process added manually. This includes showing that the ANN semantics are cyclic. There is also a `Simplified Segway Semantics` folder with a generated simpler semantics for reasoning, that is equivalent to the overall behaviour of the Segway.

The `Supporting Proofs` directory contains additional files supporting our verification strategy:
- CSPM documents supporting 
- An implementation of the ANN semantics in Isabelle/UTP. 
- In a directory `Marabou_Input_Files`, files to use Marabou to prove that an ANN component (`AnglePIDANN`) conforms to a controller (`AnglePID_C`). 
- CSPM documents that show our ANN Circus semantics are equivalent to our CSPM semantics. 

The `M2MValidation` directory contains our first validation files for the Epsilon M2M translation.

The `Validation` directory contains a Java implementation of our CSP semantics, written using the JCSP library. The directory `JCSP_JML_Previous_Version` contains 
an old version of our JCSP implementation with Java Modelling Language (JML) annotations. The directory `Python_Validation` contains a Keras implementation of our ANNs 
for validation.

The `Semantics` directory contains our semantic models. 
- A directory `Circus` contains a description of our Circus semantics.
- A directory `CSP` contains our in-development CSP semantics, our inactivation semantics.
