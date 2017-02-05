
##############################################################
########## Script to find best values for a, b, c ############
##############################################################


##############################################################
# Changes the working directory to the folder of the current file
this.dir <- NULL
tryCatch(this.dir <- dirname(sys.frame(1)$ofile), error = function(e) print('Getting file path from location of the file.'))

if(is.null(this.dir))
    this.dir <-dirname(rstudioapi::getActiveDocumentContext()$path)
if(is.null(this.dir)){
    print("Setting working directory failed. Script might fail to work.")
}else{
    setwd(this.dir)
    print(paste("Working directory changed successfully to: ", this.dir))
}


##############################################################
#################### Functions ###########################

#Zipf's Law Function
zipf <- function(rank, a, b, c) {
    return(log(c) + a*log(rank + b))
}

#Heaps' Law Function
heaps <- function(K, N, B){
    return (K * N^B)
}


##############################################################
######################### Code ###############################

# Read the words file
word_count <- read.csv("word_count.csv", col.names = c("Word","Frequency"), encoding = "UTF-8",stringsAsFactors = FALSE, sep =",")

# Add Rank
word_count$Rank = 1:nrow(word_count)

#preview
head(word_count)

#Create the df for finding the parameters (using Logarithm)
x <- log(word_count$Rank)
y <- log(word_count$Frequency)
df_wc <- data.frame(x,y)

#Put rank in variable
rank <- word_count$Rank

# Estimating the Zipf's parameters
fitted_zipf <- nls(y ~ zipf(rank, a, b, c), data = df_wc, start = list(a=-1.3, b=18, c=1000000), trace = T)

#Print final results of Zipf's parameters
print(fitted_zipf)



# Read the words by collection file
words_by_collection <- read.csv("words_by_collection.csv", col.names = c("Unique","Count"), encoding = "UTF-8",stringsAsFactors = FALSE, sep =",")
head(words_by_collection)

#Create the df for finding the parameters
x <- words_by_collection$Unique
y <- words_by_collection$Count
df_wbc <- data.frame(x,y)

#put N (unique words) in a single variable
unique <- words_by_collection$Unique


# Estimating the Zipf's parameters
fitted_heaps <- nls(y ~ heaps(k, unique, b), data = df_wbc, start = list(k=10, b=0.4), trace = T)

#Print final results of Zipf's parameters
print(fitted_heaps)

