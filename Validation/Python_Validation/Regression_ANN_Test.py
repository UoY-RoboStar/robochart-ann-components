from keras.models import Sequential
from keras.layers import Dense, Activation
import matplotlib.pyplot as plt
import numpy as np

#One way of building model
#model = Sequential([Dense(32, input_shape=(784,)),
#                    Activation('relu'),
#                    Dense(10),
#                    Activation('relu'),
#                    ])
#Another way
#model2 = Sequential()
#model2.add(Dense(32, input_dim=784))
#model2.add(Activation('relu'))

#Compilation
#model.compile(optimizer='rmsprop', loss='')
#Loss funcitons, 'categorical_crossentropy', multi class classification
#'binary_crossentropy', binary classification problem. 

# mean squared error loss funciton, adam optimiser, 
# adam, https://keras.io/api/optimizers/adam/
# used in machine learning mastery, basics of regression: https://machinelearningmastery.com/regression-tutorial-keras-deep-learning-library-python/#:~:text=The%20Keras%20wrapper%20object%20used,of%20epochs%20and%20batch%20size.

#loss function, mean squared error, for regression. 

#What should this do? 

#Train a neural network to be a linear function, x * y, automatically print out the function,
#display the function and the ANN, on the same graph, line graph. 

#Have the target function as a lambda, 
#This function visualises a neural network, with the

#Plot a 1D input with a 1D output. 
#Y = 2X is our initial Target_Function. 
#Noramlised, no normalisation for now. 

#We have two lambdas, let's train an ANN on the data. 

def visualise(ANN, target_function, in_min, in_max): 
	fig, ax = plt.subplots()
	interval = 0.01
	
	#Input data:
	Input = np.arange(in_min, in_max, interval)
	Y_Target = target_function(X)
	Y_Pred = ANN(X)
	
	ax.grid()
	ax.set_xlabel("Input")
	ax.set_ylabel("Output")
	#Plot the target
	ax.plot(X, Y_Target, color='red')
	#Plot the prediction
	ax.plot(X, Y_Pred, color='blue')
	plt.show()
	
#Trains an ANN to an arbritrary target function (target_function)
#Assumed to be: target function is 1D real function, R -> R. 
#This function returns the ANN. 

def train_ANN(target_function, in_min, in_max):
	#Interval to generate samples on
	 interval = 0.01
	 input_range = in_max - in_min
	 
	 #sample_no = int(input_range / interval)
	 #1D for now
	 sample_dimension = 1
	 
	 #training_data = np.zeros([sample_no, dim], dtype=np.float64)
	 training_data = np.arange(in_min, in_max, interval, dtype=np.float64)
	 #sample_no = np.shape(training_data)[0]
	 training_labels = target_function(training_data)
	 #print(training_labels)
	 #Build ANN
	 ANN = Sequential()
	 ANN.add(Dense(10, activation='relu', input_dim=1))
	 ANN.add(Dense(1, activation='linear'))
	 
	 ANN.compile(optimizer='adam', loss='mean_squared_error', metrics=['accuracy'])
	 #removing metrics. 
	 ANN.fit(training_data, training_labels, epochs=5, batch_size=10)
	 
	 
	 test_data = np.zeros([1])
	 test_data[0] = 50

	 
	 print("prediction", ANN.predict(test_data))
	 
	 
	 
	 
	 
	 
def build_TN1_ReLU():
    TN1_ReLU = Sequential()
    TN1_ReLU.add(Dense(32, activation='relu', input_dim=2))
    TN1_ReLU.add(Dense(32, activation='relu'))
    TN1_ReLU.add(Dense(1, activation='relu'))
    TN1_ReLU.compile(optimizer='rmsprop', 
                  loss='binary_crossentropy', 
                  metrics=['accuracy'])
    
    training_data, training_labels = generate_training_data()
    TN1_ReLU.fit(training_data, training_labels, epochs=100, batch_size = 32)
    #Complete list of all weights and biases for this neural network. 
    complete_params = TN1_ReLU.get_weights()
    #Slice to even and odd indexes of the list.
    weights = complete_params[::2]
    biases = complete_params[1::2]

def load_TN1_ReLU():
    TN1_ReLU = Sequential()
    TN1_ReLU.add(Dense(32, activation='relu', input_dim=2))
    TN1_ReLU.add(Dense(32, activation='relu'))
    TN1_ReLU.add(Dense(1, activation='relu'))
    TN1_ReLU.compile(optimizer='rmsprop', 
                  loss='binary_crossentropy', 
                  metrics=['accuracy'])

    #Random initialization of weights

    #Set to zero.
    weight_0, weight_1, weight_2, bias_0, bias_1, bias_2 = load_w_b()

    complete_params = (weight_0, bias_0, weight_1, bias_1, weight_2, bias_2)
    TN1_ReLU.set_weights(complete_params)

    stochastic_test(TN1_ReLU, 10000000)

# Returns weights and biases of TN1, 32x2, 32x32, 1x32.
# And bias vectors 32, 32, 1.
def load_w_b():
    weight_0 = load_csv('TN1_ReLU/TN1_ReLU_weight_0.csv')
    weight_0 = weight_0.T
    weight_1 = load_csv('TN1_ReLU/TN1_ReLU_weight_1.csv')
    weight_1 = weight_1.T
    weight_2 = load_csv('TN1_ReLU/TN1_ReLU_weight_2.csv')
    weight_2 = weight_2.T
    weight_2 = np.reshape(weight_2, (32, 1))

    bias_0 = load_csv('TN1_ReLU/TN1_ReLU_bias_0.csv')
    bias_1 = load_csv('TN1_ReLU/TN1_ReLU_bias_1.csv')
    bias_2 = load_csv('TN1_ReLU/TN1_ReLU_bias_2.csv')
    bias_2 = np.array([bias_2])


    return weight_0, weight_1, weight_2, bias_0, bias_1, bias_2

def load_csv(file_name):
    ret = np.loadtxt(file_name, delimiter=',')
    return ret

def save_csv(weights, biases, training_data, training_labels):
    for i in range(len(weights)):
        np.savetxt('TN1_ReLU\TN1_ReLU_weight_' + str(i) + '.csv', weights[i].T, delimiter=',')
        np.savetxt('TN1_ReLU\TN1_ReLU_bias_' + str(i) + '.csv', biases[i].T, delimiter=',')
    
    train_ = np.zeros((100, 3))
    train_[:, 0:2] = training_data
    train_[:, 2] = training_labels
    print(train_)
    np.savetxt('TN1_ReLU\TN1_ReLU_training_data.csv', train_, delimiter=',')


def stochastic_test(TN1, num_samples):
    #Randomly sample area of Class 1 and see if it really is safe, to verify results obtained by NNV.
    num_dim = 2

    test_data = np.zeros([num_samples, num_dim])

    #test_data[:, 0] =  np.random.normal(low = 2.6, high = 10, size = (num_samples, ))
    #test_data[:, 1] = np.random.normal(low = -10, high = -2.6, size = (num_samples,))
    test_data[:, 0] = np.zeros((num_samples, ))
    test_data[:, 1] = np.zeros((num_samples, ))
    
    predictions = TN1.predict(test_data)
    min = np.min(predictions)
    max = np.max(predictions)
    print("Bounds from Stochastic Test: [", min, ", ", max, "]")

def generate_training_data():
    npoints = 100
    dim = 2
    half = int(npoints/2)
    
    training_data = np.zeros([npoints, dim], dtype = np.float64)
    training_labels = np.zeros([npoints])

    # Class 1:
    training_data[:half, 0] = np.random.uniform(low = -10, high = 2.5, size = (half,))
    training_data[:half, 1] = np.random.uniform(low = -2.5, high = 10, size = (half,))

    # Class 2:

    training_data[half:, 0] =  np.random.uniform(low = -2.5, high = 10, size = (half, ))
    training_data[half:, 1] = np.random.uniform(low = -10, high = 2.5, size = (half,))

    training_labels[:half] = 0
    training_labels[half:] = 1

    #plt.figure()
    #plt.scatter(training_data[:, 0], training_data[:, 1], c=training_labels)
    #plt.show()

    return training_data, training_labels

if(__name__ == "__main__"):
    train_ANN(lambda x : x * 2, 1, 100)
    #visualise((lambda x : x * 2), (lambda x : x * 3), 0, 100)
