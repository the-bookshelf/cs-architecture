lorem = """
Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam ac pulvinar mi. Proin nec mollis
tellus. In neque risus, rhoncus nec tellus eu, laoreet faucibus eros. Ut malesuada dui vel ipsum
ultrices posuere. Aenean ut laoreet ante. Donec auctor vehicula dui. Nulla facilisi. Orci varius
natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Nunc tempus hendrerit
bibendum. Maecenas lobortis magna luctus suscipit sollicitudin. Quisque sem eros, porta et finibus
at, ornare ut elit.

In semper risus ac aliquet hendrerit. Nam erat enim, congue non congue ac, tincidunt in urna.
Vestibulum pharetra ut libero et suscipit. Phasellus quis lectus et eros convallis tempus id at
diam. Sed vel ipsum nisi. Ut in ornare augue. Nunc nec augue in mauris viverra tincidunt a sit amet
lorem. Aliquam nibh lectus, mollis euismod porta eget, luctus sit amet nunc. Donec non est non
dolor posuere congue. Mauris ex erat, aliquam tincidunt scelerisque ac, pellentesque ac tortor.
Phasellus maximus nibh eu sapien tempor, et congue ante ultrices. Curabitur ut est turpis. Nunc
dictum odio at felis aliquet faucibus. In elementum tempus risus vitae fringilla. Nam mollis cursus
ligula, vitae tempor ante faucibus et.

Phasellus rutrum malesuada leo, ut tincidunt mauris consectetur a. Praesent consectetur iaculis
lectus, congue viverra nunc finibus sit amet. Nulla ultrices neque vel tortor faucibus consequat.
Sed sit amet dolor eget urna sollicitudin laoreet. Aliquam a bibendum eros, vitae gravida ante. Sed
ac enim orci. Sed fermentum hendrerit sagittis. Aliquam erat volutpat. Sed bibendum porttitor
lacinia. Aliquam eleifend auctor mi at ullamcorper. Ut scelerisque maximus cursus. Class aptent
taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Aenean eleifend ut
metus eu interdum. Cras pulvinar, leo nec scelerisque dapibus, est magna feugiat justo, in
venenatis lacus ligula tincidunt risus. Integer ac accumsan lorem.

Sed tristique sit amet dui vitae mollis. Donec pretium, metus non ultricies consequat, metus diam
porttitor sem, porttitor accumsan urna tortor eget enim. Donec mattis, lorem nec porta molestie,
nibh tortor feugiat augue, a dignissim est orci sit amet ligula. Integer auctor sit amet nisl ac
mollis. Integer lacinia bibendum velit et porta. Fusce luctus ante non nisi vestibulum volutpat.
Aliquam mattis quis ligula at fermentum. Pellentesque habitant morbi tristique senectus et netus et
malesuada fames ac turpis egestas. Donec mattis sagittis lorem in pharetra. Proin gravida dignissim
vestibulum. Nulla feugiat quam nunc, sed finibus massa ornare eu. Mauris cursus ex non mi commodo
mattis. In ac lectus massa. Pellentesque sollicitudin id est ut ultrices.

Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Maecenas
ultrices dictum rutrum. Fusce sit amet risus blandit, sodales urna eget, luctus nunc. Integer ut
elit consectetur, placerat nisi a, pretium enim. Donec dapibus enim in ullamcorper posuere. Vivamus
venenatis ullamcorper ex sed eleifend. Nam nec pharetra elit.
"""

words = (w.strip() for w in lorem.split())

def mapper(word):
    return (word, 1)

def reducer(mapper_results):
    results = {}
    for (word, count) in mapper_results:
        if word in results:
            results[word] += count
        else:
            results[word] = count
    return total_len / num_words

def mapper(word):
    return len(word)

def reducer(mapper_results):
    results = list(mapper_results)
    print(results)
    total_words = len(results)
    total_len = sum(results)
    return total_len / total_words


mapper_results = map(mapper, words)
reducer_results = reducer(mapper_results)
print(reducer_results)
