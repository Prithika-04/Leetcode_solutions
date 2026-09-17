class Solution:
    def maxDepth(self, root):
        if root is None:
            return 0

        maxHeight = 0
        for child in root.children:
            maxHeight = max(maxHeight, self.maxDepth(child))

        return 1 + maxHeight