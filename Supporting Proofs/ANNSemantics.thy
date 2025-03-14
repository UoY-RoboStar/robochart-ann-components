theory ANNSemantics
  imports "UTP1-Circus.utp_circus" Complex_Main
begin


type_synonym layerI = nat
type_synonym nodeI = nat 
type_synonym annindex = nat
type_synonym Value = real


datatype InOut = In | Out

alphabet st_ann =
  annsum :: real
  rec_index :: int
  final_output :: real

chantype ch_ann = 
  leftMotorVelocity :: "InOut \<times> real"
  rightMotorVelocity :: "InOut \<times> real"
  angle :: "InOut \<times> real"
  gyroX :: "InOut \<times> real"
  gyroY :: "InOut \<times> real"
  gyroZ :: "InOut \<times> real"
  setLeftMotorSpeedCall :: "real" 
  setRightMotorSpeedCall :: "real"
  enableInterruptsCall :: "nat"
  disableInterruptsCall :: "nat"

  adiff :: "InOut \<times> real"
  anewError :: "InOut \<times> real"
  angleOutputE :: "InOut \<times> real"

  layerRes :: "layerI \<times> nodeI \<times> Value" 
  nodeOut :: "layerI \<times> nodeI \<times> annindex \<times> Value"


type_synonym action_ann_dt = "(st_ann, ch_ann) action"


definition relu :: "(\<real>, 'a) uexpr \<Rightarrow> (\<real>, 'a) uexpr" where
"relu x = max\<^sub>u(x, 0)"

(*Constants*)

(*Constants of the ANN semantics*)

definition insize :: "nat" where
"insize = 2"

definition outsize :: "nat" where
"outsize = 1"

definition layerstructure :: "nat list" where
"layerstructure = [1, 1]"



definition layerNo :: "nat"  where
"layerNo = 2"

definition maxSize :: "nat" where
"maxSize = 1"

(*TODO: update, List datatype appears to have changed from 2021*)
definition weights :: "real list list list" where
"weights = [[[1.22838,0.132874]], [[0.744636]]]"

definition biases :: "real list list" where 
"biases = [[0.125424], [-0.107753]]"

(*The actual definitions, and showing they are equal to the reactive contracts work*)
(*Original signature is: layerI \<Rightarrow> nodeI \<Rightarrow> annindex \<Rightarrow>, *)
definition NodeIn :: "layerI \<Rightarrow> nodeI \<Rightarrow> annindex \<Rightarrow> action_ann_dt" where
"NodeIn layer node index = 
  layerRes.(\<guillemotleft>layer\<guillemotright> - 1).(\<guillemotleft>node\<guillemotright>)?(x) \<^bold>\<rightarrow> nodeOut.(\<guillemotleft>layer\<guillemotright>).(\<guillemotleft>node\<guillemotright>).(\<guillemotleft>index\<guillemotright>).(\<guillemotleft>x\<guillemotright> * \<guillemotleft>weights layer node index\<guillemotright>) \<^bold>\<rightarrow> Skip"

definition NodeInConcrete :: "layerI \<Rightarrow> nodeI \<Rightarrow> annindex \<Rightarrow> action_ann_dt" where
"NodeInConcrete layer node index = 
  layerRes.(\<guillemotleft>layer\<guillemotright> - 1).(\<guillemotleft>node\<guillemotright>).(0) \<^bold>\<rightarrow> nodeOut.(\<guillemotleft>layer\<guillemotright>).(\<guillemotleft>node\<guillemotright>).(\<guillemotleft>index\<guillemotright>).(0) \<^bold>\<rightarrow> Skip"

(*Production functions, to get the functions from channels*)
(*Given the layer and node, returns a set of all event products of type (ch_ann)
From layerRes, and gives all value as type real, evaluated across all reals*)
definition layerResProd :: "layerI \<Rightarrow> nodeI \<Rightarrow> ch_ann set" where
"layerResProd layer node = {build\<^bsub>layerRes\<^esub> (layer,node, value) | value. value \<in> \<real>}"

(*Returns a single event of layerRes*)
definition layerResSingle :: "layerI \<Rightarrow> nodeI \<Rightarrow> real \<Rightarrow> ch_ann" where
[rdes_def]:"layerResSingle layer node val = build\<^bsub>layerRes\<^esub> (layer,node,val)"

(*Layer res productions, but just the layer*)
definition layerResProdL :: "layerI \<Rightarrow> ch_ann set" where
"layerResProdL layer = {build\<^bsub>layerRes\<^esub> (layer,node, value) |
                                        node value.
                                           node \<in> {1..maxSize} \<and>
                                           value \<in> \<real>}"

(*Returns every production of nodeOut*)
definition nodeOutAll :: "ch_ann set" where
"nodeOutAll = {build\<^bsub>nodeOut\<^esub> (layer, node, index, value) |
                                 layer node index value. 
                                    layer \<in> {0..layerNo} \<and>
                                    node \<in> {1..maxSize} \<and>
                                    index \<in> {0..maxSize} \<and>
                                    value \<in> \<real>}"

(*Returns the production of nodeOut given the layer and node*)
definition nodeOutProd :: "layerI \<Rightarrow> nodeI \<Rightarrow> ch_ann set" where
"nodeOutProd layer node = {build\<^bsub>nodeOut\<^esub> (layer, node, index, value) |
                                 index value. 
                                    index \<in> {0..maxSize} \<and>
                                    value \<in> \<real>}"

(*Equation 1 of C, local definition of Collator*)
definition C1 :: "layerI \<Rightarrow> nodeI \<Rightarrow> action_ann_dt" where
"C1 layer node = layerRes.(\<guillemotleft>layer\<guillemotright>).(\<guillemotleft>node\<guillemotright>).(relu &annsum + \<guillemotleft>biases layer node\<guillemotright>) \<^bold>\<rightarrow> Skip"

definition C2 :: "layerI \<Rightarrow> nodeI \<Rightarrow> action_ann_dt" where
"C2 layer node = nodeOut.(\<guillemotleft>layer\<guillemotright>).(\<guillemotleft>node\<guillemotright>).(&rec_index)?(x) \<^bold>\<rightarrow> (rec_index :=\<^sub>C (&rec_index - 1) ;; annsum :=\<^sub>C (&annsum + \<guillemotleft>x\<guillemotright>));;Skip"

definition C :: "layerI \<Rightarrow> nodeI \<Rightarrow> action_ann_dt" where
"C layer node = (\<mu>\<^sub>C X \<bullet> (C1 layer node) \<triangleleft> (&rec_index =\<^sub>u 0) \<triangleright>\<^sub>R ((C2 layer node) ;; X))"

definition Collator :: "layerI \<Rightarrow> nodeI \<Rightarrow> annindex \<Rightarrow> action_ann_dt" where
"Collator layer node index = (rec_index :=\<^sub>C \<guillemotleft>index\<guillemotright>);;(C layer node)"

(*Recursive function to build a list of n nodeIns, layer node x*)
fun build_nodein_list :: "layerI \<Rightarrow> nodeI \<Rightarrow> nat \<Rightarrow> action_ann_dt list" where
"build_nodein_list _ _ 0 = []" |
"build_nodein_list layer node index = (NodeIn layer node index) # build_nodein_list layer node (index-1)"

definition Node :: "layerI \<Rightarrow> nodeI \<Rightarrow> nat \<Rightarrow> action_ann_dt" where
"Node layer node inputSize = 
  ( 
    hide\<^sub>r
    (fold (|||) (build_nodein_list layer node inputSize) Skip
     \<lbrakk>  nodeOutProd layer node  \<rbrakk>\<^sub>C
    Collator layer node inputSize) 
    
    ( nodeOutAll )
  )"

(*size renamed to l_size, existing operation*)
definition HiddenLayer :: "layerI \<Rightarrow> nat \<Rightarrow> nat \<Rightarrow> action_ann_dt" where
"HiddenLayer layer l_size inputSize = (Node layer l_size inputSize)"

definition HiddenLayers :: "action_ann_dt" where
"HiddenLayers = HiddenLayer 1 (layerstructure 1) insize"

definition OutputLayer :: "action_ann_dt" where
"OutputLayer = (Node 2 (layerstructure 2) (layerstructure 1))"

(*Sigma except 0, 2, is just hiding 1*)
definition AnglePIDANN :: "action_ann_dt" where
"AnglePIDANN = hide\<^sub>r (HiddenLayers \<lbrakk>layerResProdL 1 \<rbrakk>\<^sub>C OutputLayer) (layerResProdL 1)"


(*Lemma 4.1 Implementation (Marabou Conformance) *)

lemma hol_proof:
  fixes x::real and y::real and e::real 
  assumes "e = 0.085"
  shows
  "
  \<forall>x \<in> {0..1}. \<forall>y \<in> {0..1}.
    
    ((max (0.744636 * (max (1.22838 * x)  0) + (0.132874 * y) + 0.125424)  0) - 0.107753) \<ge>
       (0.9230769 * x) + (0.076923075 * y) - e
     \<and>
    ((max (0.744636 * (max (1.22838 * x)  0) + (0.132874 * y) + 0.125424)  0) - 0.107753)
     \<le> (0.9230769 * x) + (0.076923075 * y) + e 
    
    
     "
  using assms(1) and max_def
  apply (simp add: max_def split:if_split_asm) 
  done
