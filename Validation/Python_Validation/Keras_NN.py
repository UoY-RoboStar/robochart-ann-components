#Class that builds, tests and saves a keras neural network model, because is easier for interpretation, 
#and already have in load_tn1 a method to convert it, partially, to eran .tf file format, saves it as easily loadable
#into matlab, for nnv, csv files.

#import keras

from keras.models import Sequential
from keras.layers import Dense, Activation
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

#epochs, need to choose, and batch size, 
#Training
#Training same parameter example but with relu final activation function.
#TN1_ReLU
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
    
    #stochastic_test(TN1_ReLU, 10000000)

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
    load_TN1_ReLU()
